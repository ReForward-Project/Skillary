'use client';

import { useEffect, Suspense } from 'react'; // 1. Suspense 추가
import { useSearchParams, useRouter } from 'next/navigation';
import { createCard } from '@/api/payments';
import Loading from '@/components/Loading'

// 2. 실제 로직을 담은 컴포넌트
function SuccessContent() {
    const searchParams = useSearchParams();
    const router = useRouter();

    const authKey = searchParams.get('authKey');
    const customerKey = searchParams.get('customerKey');

    useEffect(() => {
        const registerCard = async () => {
            if (!authKey || !customerKey) return;
            try {
                await createCard(customerKey, authKey);
                router.replace('/cards/list');
            } catch (error) {
                // error 객체에서 상세 정보를 추출하거나 기본값 설정
                const code = error.code || 400;
                const msg = error.message || '등록 실패';
                router.push(`/payments/fail?code=${code}&message=${msg}`);
            }
        };

        registerCard();
    }, [authKey, customerKey, router]);

    return <Loading loadingMessage='카드 등록중입니다. 잠시만 기다려주세요.'/>
}

// 3. 빌드 에러 방지를 위한 메인 컴포넌트 (Suspense 적용)
export default function SuccessPage() {
    return (
        <Suspense fallback={<Loading loadingMessage='페이지를 로딩 중입니다...' />}>
            <SuccessContent />
        </Suspense>
    );
}