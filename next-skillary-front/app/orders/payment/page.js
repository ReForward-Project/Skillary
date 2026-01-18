'use client';

import useSWR from 'swr';
import { useSearchParams } from 'next/navigation';

import { singleOrder, restartOrder } from '@/api/payments';
import { confirmSinglePay } from '@/api/tossPayments';
import Loading from '@/components/Loading';
import CardFailPage from '@/components/CardFailPage';

import OrderHeader from '../components/OrderHeader';
import OrderSummary from '../components/OrderSummary';
import OrderFooter from '../components/OrderFooter';
import OrderDescriptionSection from '../components/OrderDescriptionSection';
import OrderPayExecution from '../components/OrderPayExecution';
import OrderLayout from '../components/OrderLayout';


export default function SingleOrderPage() {
  const searchParams = useSearchParams();

  const orderId = searchParams.get('orderId');
  const contentId = searchParams.get('contentId');

  const { data: orderResponse, error, isLoading } = useSWR(
    (orderId || contentId) ? ['single-order', orderId, contentId] : null,
    async () => {
      if (orderId) return await restartOrder(orderId);
      if (contentId) return await singleOrder(contentId);
      return null;
    }
  );

  if (isLoading) 
    return <Loading loadingMessage='주문 정보를 로딩중입니다...'/>

  if (!orderResponse) {
    return (
      <CardFailPage
        errorCode='404'
        errorMessage='NOT_FOUND'
        failUrl='/orders/list'
        failUrlDesc='주문 목록으로 돌아가기'
      />
    );
  }

  const handlePayment = async () => {
    try {
      console.log('결제 승인 중...');
      await confirmSinglePay(
        orderResponse.customerKey,
        orderResponse.orderId,
        orderResponse.contentTitle,
        orderResponse.price
      );
      console.log('결제 승인 완료');
    } catch (e) {
      router.push(`/payments/fail?code=${e.code || 400}&message=${e.message}`);
    }
  };

  return (
      <>
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
        </>
    // <div className="min-h-screen bg-gray-50">
    //   <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    //     {/* 페이지 헤더 */}
    //     <div className="mb-8">
    //       <h1 className="text-3xl font-bold text-black mb-2">주문 정보</h1>
    //       <p className="text-gray-600">주문 내용을 확인하고 결제를 진행해주세요</p>
    //     </div>

    //     <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
    //       {/* 주문 상세 정보 */}
    //       <div className="lg:col-span-2 space-y-6">
    //         {/* 크리에이터/콘텐츠 정보 */}
    //         <div className="bg-white rounded-lg shadow-sm p-6">
    //           <h2 className="text-xl font-bold text-black mb-4">'싱글 콘텐츠'</h2>
    //           <div className="flex gap-4">
    //             <div className={`w-24 h-24 rounded-lg bg-gradient-to-br flex items-center justify-center flex-shrink-0`}>
    //               <div className="text-4xl">{orderResponse.creatorName}</div>
    //             </div>
    //             <div className="flex-1">
    //               <h3 className="text-lg font-semibold text-black mb-2">{orderResponse.contentTitle}</h3>
    //               <div className="flex items-center gap-2 mb-2">
    //                 <div className="w-6 h-6 rounded-full bg-gray-300"></div>
    //                 <span className="text-sm text-gray-600">6</span>
    //               </div>
    //               <p className="text-sm text-gray-600 line-clamp-2">7</p>
    //             </div>
    //           </div>
    //         </div>

    //         {/* 구독 플랜 정보 */}
    //         <OrderDescriptionSection/>

    //         {/* 결제 정보 */}
    //         <div className="bg-white rounded-lg shadow-sm p-6">
    //           <h2 className="text-xl font-bold text-black mb-4">결제 정보</h2>
    //           <div className="space-y-3">
    //             <div className="flex justify-between text-sm">
    //               <span className="text-gray-600">구독료</span>
    //               <span className="text-black">1111111</span>
    //             </div>
    //             <div className="border-t border-gray-200 pt-3 flex justify-between">
    //               <span className="font-semibold text-black">총 결제금액</span>
    //               <span className="text-xl font-bold text-black">2222222</span>
    //             </div>
    //           </div>
    //         </div>
    //       </div>

    //       {/* 주문 요약 및 결제 버튼 */}
    //       <div className="lg:col-span-1">
    //         <div className="bg-white rounded-lg shadow-sm p-6 sticky top-8">
    //           <h2 className="text-xl font-bold text-black mb-4">주문 요약</h2>
    //           <div className="space-y-4 mb-6">
    //             <div>
    //               <p className="text-sm text-gray-600 mb-1">구독 플랜</p>
    //               <p className="font-semibold text-black">-------</p>
    //             </div>
    //             <div>
    //               <p className="text-sm text-gray-600 mb-1">결제 주기</p>
    //               <p className="font-semibold text-black">매월 자동 결제</p>
    //             </div>
    //             <div>
    //               <p className="text-sm text-gray-600 mb-1">다음 결제일</p>
    //               <p className="font-semibold text-black">다음 달 오늘</p>
    //             </div>
    //             <div className="border-t border-gray-200 pt-4">
    //               <div className="flex justify-between items-center mb-2">
    //                 <span className="text-gray-600">총 금액</span>
    //                 <span className="text-2xl font-bold text-black">₩{orderResponse.price}</span>
    //               </div>
    //               <p className="text-xs text-gray-500">매월 동일 금액이 자동으로 결제됩니다</p>
    //             </div>
    //           </div>
    //           <button
    //             onClick={handlePayment}
    //             className="w-full bg-black text-white py-3 rounded-lg font-semibold hover:bg-gray-800 transition mb-4"
    //           >
    //             결제하기
    //           </button>
    //           <Link
    //             href={`/contents/${contentId}`}
    //             className="block w-full text-center py-2 text-gray-600 hover:text-black transition text-sm"
    //           >
    //             취소
    //           </Link>
    //         </div>
    //       </div>
    //     </div>
    //   </div>
    // </div>
  );
}
