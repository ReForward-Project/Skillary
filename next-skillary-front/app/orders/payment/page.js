'use client';

import { Suspense } from 'react'; // 1. Suspense 임포트
import useSWR from 'swr';
import { useSearchParams } from 'next/navigation';

import { paymentOrder, retrieveOrder } from '@/api/payments';
import { confirmSinglePay } from '@/api/tossPayments';
import Loading from '@/components/Loading';
import CardFailPage from '@/components/CardFailPage';

import OrderHeader from '../components/OrderHeader';
import OrderSummary from '../components/OrderSummary';
import OrderFooter from '../components/OrderFooter';
import OrderDescriptionSection from '../components/OrderDescriptionSection';
import OrderPayExecution from '../components/OrderPayExecution';
import OrderLayout from '../components/OrderLayout';

// 2. 실제 결제 로직을 담당하는 컴포넌트 분리
function PaymentContent() {
  const searchParams = useSearchParams(); // 클라이언트 측에서만 동작

  const orderId = searchParams.get('orderId');
  const contentId = searchParams.get('contentId');

  const { data: orderResponse, error, isLoading } = useSWR(
    (orderId || contentId) ? ['single-order', orderId, contentId] : null,
    async () => {
      if (orderId) return await retrieveOrder(orderId);
      if (contentId) return await paymentOrder(contentId);
      return null;
    }
  );

  if (isLoading) 
    return <Loading loadingMessage='주문 정보를 로딩중입니다...'/>

  if (!orderResponse)
    return <CardFailPage
        errorCode='404'
        errorMessage='NOT_FOUND'
        failUrl='/orders/list'
        failUrlDesc='주문 목록으로 돌아가기'/>

  const handlePayment = async () => {
    try {
      await confirmSinglePay(
        orderResponse.customerKey,
        orderResponse.orderId,
        orderResponse.contentTitle,
        orderResponse.price
      );
    } catch (e) {
      // router 사용 시 useRouter() 훅 정의 필요
      window.location.href = `/payments/fail?code=${e.code || 400}&message=${e.message}`;
    }
  };

  return (
      <OrderLayout
        orderHeader={ <OrderHeader /> }
        orderSummary={
          <OrderSummary
            title='싱글 콘텐츠'
            icon='📦'
            contentTitle='콘텐츠 제목'
            description='콘텐츠 설명'
            gradientColors='from-blue-400 to-purple-500'
          />
        }
        orderDescription={
          <OrderDescriptionSection
            planName={orderResponse.contentTitle}
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
            planName={orderResponse.contentTitle}
            paymentCycle='단건 결제'
            totalAmount={orderResponse.price}
            onPaymentClick={handlePayment}
            isSticky={true}
          />
        }/>
  );
}

// 3. 빌드 에러 방지를 위한 메인 컴포넌트 (Suspense 적용)
export default function PaymentOrderPage() {
  return (
    <Suspense fallback={<Loading loadingMessage='페이지를 준비중입니다...' />}>
      <PaymentContent />
    </Suspense>
  );
}