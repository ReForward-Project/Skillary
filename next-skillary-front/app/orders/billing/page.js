'use client';

import { Suspense } from 'react'; // Suspense 추가
import useSWR from 'swr';
import { useRouter, useSearchParams } from 'next/navigation';

import { completeBilling, billingOrder, retrieveOrder } from '@/api/payments';
import CardFailPage from '@/components/CardFailPage';
import Loading from '@/components/Loading';

import OrderHeader from '../components/OrderHeader';
import OrderSummary from '../components/OrderSummary';
import OrderFooter from '../components/OrderFooter';
import OrderDescriptionSection from '../components/OrderDescriptionSection';
import OrderPayExecution from '../components/OrderPayExecution';
import OrderLayout from '../components/OrderLayout';

// 실제 결제 로직 컴포넌트
function BillingContent() {
  const searchParams = useSearchParams();
  const router = useRouter();

  const orderId = searchParams.get('orderId');
  const planId = searchParams.get('planId');

  const { data: orderResponse, error, isLoading } = useSWR(
    (!orderId && !planId) ? null : ['plan-order', orderId, planId],
    async () => {
      if (orderId) return await retrieveOrder(orderId)
      if (planId) return await billingOrder(planId);
      return null;
    }
  )

  if (isLoading) return <Loading loadingMessage='주문 정보를 로딩중입니다...'/>

  if (!orderResponse) {
    return <CardFailPage
      errorCode='NOT_FOUND'
      errorMessage='주문 정보를 찾을 수 없습니다.'
      failUrl='/creators'
      failUrlDesc='크리에이터 목록으로 돌아가기'
    />;
  }

  const handlePayment = async () => {
    try {
      const result = await completeBilling({
        customerKey: orderResponse.customerKey,
        orderId: orderResponse.orderId,
        planName: orderResponse.planName,
        subscriptionFee: orderResponse.price
      })
      const paymentKey = result.paymentKey;
      const orderId = result.orderId;
      const price = result.amount;
      router.push(`/payments/success?paymentKey=${paymentKey}&orderId=${orderId}&price=${price}`);
    } catch (e) {
      if (e.code && e.code === "USER_CANCEL")
        console.log("사용자가 결제창을 닫음");

      router.push('/payments/fail');
    }
  };

  return (
    <OrderLayout
      orderHeader={<OrderHeader />}
      orderSummary={
        <OrderSummary
          title='구독 콘텐츠'
          icon='📦'
          contentTitle='콘텐츠 제목'
          description='콘텐츠 설명'
          gradientColors='from-blue-400 to-purple-500'
        />
      }
      orderDescription={
        <OrderDescriptionSection
          planName={orderResponse.planName}
          price={orderResponse.price}
        />
      }
      orderFooter={
        <OrderFooter
          subscriptionFee={orderResponse.price}
          totalAmount={orderResponse.price}
        />
      }
      orderPayExecution={
        <OrderPayExecution
          sectionTitle='주문 요약'
          planName={orderResponse.planName}
          paymentCycle='매월 자동 결제'
          totalAmount={orderResponse.price}
          onPaymentClick={handlePayment}
          isSticky={true}
        />
      }/>
  );
}

// 빌드 에러 해결을 위한 메인 페이지 컴포넌트
export default function BillingOrderPage() {
  return (
    <Suspense fallback={<Loading loadingMessage='결제 페이지를 불러오는 중입니다...' />}>
      <BillingContent />
    </Suspense>
  );
}