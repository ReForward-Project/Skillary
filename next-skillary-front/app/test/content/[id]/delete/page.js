'use client';

import { useState } from 'react';
import { useParams, useRouter } from 'next/navigation';

const API_URL = process.env.NEXT_PUBLIC_FRONT_API_URL || 'http://localhost:8080/api';

export default function ContentDeleteTestPage() {
  const params = useParams();
  const router = useRouter();
  const contentId = params.id;
  
  const [deleteContentId, setDeleteContentId] = useState(contentId || '');
  const [deleting, setDeleting] = useState(false);
  const [result, setResult] = useState(null);

  // 콘텐츠 삭제
  const handleDeleteContent = async () => {
    if (!deleteContentId) {
      alert('콘텐츠 ID를 입력해주세요.');
      return;
    }

    setDeleting(true);
    setResult(null);

    try {
      console.log('[DELETE] 콘텐츠 삭제 요청:', deleteContentId);
      
      const res = await fetch(`${API_URL}/contents/${deleteContentId}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      console.log('[DELETE] 응답 상태:', res.status);

      if (!res.ok) {
        const errorText = await res.text();
        console.error('[DELETE] 삭제 실패:', errorText);
        throw new Error(`콘텐츠 삭제 실패 (${res.status}): ${errorText}`);
      }

      const resultData = { success: true, message: '콘텐츠 삭제 성공', status: res.status };
      console.log('[DELETE] 콘텐츠 삭제 성공:', resultData);
      setResult(resultData);
      setDeleteContentId('');
    } catch (err) {
      console.error('[DELETE] 콘텐츠 삭제 실패:', err);
      setResult({ success: false, message: err.message, error: err });
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-2xl mx-auto">
        <h1 className="text-3xl font-bold mb-6">콘텐츠 삭제 테스트</h1>
        
        <div className="bg-white rounded-lg border border-gray-200 p-6 mb-6">
          <h2 className="text-xl font-semibold mb-4">삭제 요청</h2>
          
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              콘텐츠 ID
            </label>
            <input
              type="text"
              placeholder="콘텐츠 ID를 입력하세요"
              value={deleteContentId}
              onChange={(e) => setDeleteContentId(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
            />
          </div>

          <button 
            onClick={handleDeleteContent}
            disabled={deleting || !deleteContentId}
            className="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 disabled:bg-gray-300 disabled:cursor-not-allowed font-semibold"
          >
            {deleting ? '삭제 중...' : '콘텐츠 삭제'}
          </button>
        </div>

        {result && (
          <div className={`bg-white rounded-lg border p-6 ${result.success ? 'border-green-200 bg-green-50' : 'border-red-200 bg-red-50'}`}>
            <h2 className="text-xl font-semibold mb-2">{result.success ? '✅ 성공' : '❌ 실패'}</h2>
            <p className="text-gray-700">{result.message}</p>
            {result.status && (
              <p className="text-sm text-gray-600 mt-2">HTTP Status: {result.status}</p>
            )}
            {result.error && (
              <pre className="mt-4 p-3 bg-gray-100 rounded text-xs overflow-auto">
                {JSON.stringify(result.error, null, 2)}
              </pre>
            )}
          </div>
        )}

        <div className="mt-6">
          <button
            onClick={() => router.push('/test/content')}
            className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
          >
            목록으로 돌아가기
          </button>
        </div>
      </div>
    </div>
  );
}

