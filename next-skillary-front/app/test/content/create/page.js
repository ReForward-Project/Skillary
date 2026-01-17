'use client';

import { useState, useRef, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Editor from '@toast-ui/editor';
import '@toast-ui/editor/dist/toastui-editor.css';
import { getCategories, createContent } from '../../../api/contents';
import { uploadImage } from '../../../api/files';

const API_URL = process.env.NEXT_PUBLIC_FRONT_API_URL || 'http://localhost:8080/api';

export default function ContentCreateTestPage() {
  const router = useRouter();
  const editorRef = useRef(null);
  const editorDivRef = useRef(null);
  const thumbnailInputRef = useRef(null);

  const [formData, setFormData] = useState({
    title: '',
    description: '',
    category: 'IT',
    type: 'free',
    paymentType: 'subscription',
    price: '',
    planId: '',
    thumbnail: null,
    thumbnailUrl: '',
    postBody: ''
  });

  const [categories, setCategories] = useState([]);
  const [creating, setCreating] = useState(false);
  const [result, setResult] = useState(null);
  
  // 보낼 DTO JSON 계산
  const getRequestDto = () => {
    return {
      title: formData.title,
      description: formData.description,
      category: formData.category,
      price: formData.type === 'paid' && formData.paymentType === 'one-time' ? parseInt(formData.price.replace(/,/g, '')) : null,
      planId: formData.type === 'paid' && formData.paymentType === 'subscription' ? parseInt(formData.planId) : null,
      thumbnailUrl: formData.thumbnailUrl || null,
      post: formData.postBody ? {
        body: formData.postBody,
        postFiles: []
      } : null
    };
  };

  // 카테고리 로드
  useEffect(() => {
    async function loadCategories() {
      try {
        const data = await getCategories();
        console.log('[GET] 카테고리 목록:', data);
        setCategories(data);
      } catch (err) {
        console.error('카테고리 로드 실패:', err);
      }
    }
    loadCategories();
  }, []);

  // ToastUI Editor 초기화
  useEffect(() => {
    if (!editorDivRef.current || editorRef.current) return;

    editorRef.current = new Editor({
      el: editorDivRef.current,
      initialValue: '',
      previewStyle: 'vertical',
      height: '400px',
      initialEditType: 'wysiwyg',
      hideModeSwitch: true,
      usageStatistics: false,
      toolbarItems: [
        ['heading', 'bold', 'italic', 'strike'],
        ['hr', 'quote'],
        ['ul', 'ol', 'task', 'indent', 'outdent'],
        ['table', 'image', 'link'],
        ['code', 'codeblock'],
      ],
      hooks: {
        addImageBlobHook: async (blob, callback) => {
          try {
            const file = new File([blob], `image-${Date.now()}.png`, { type: blob.type });
            console.log('[UPLOAD] 이미지 업로드 시작:', file.name);
            const imageUrl = await uploadImage(file);
            console.log('[UPLOAD] 이미지 업로드 완료:', imageUrl);
            callback(imageUrl, '이미지');
          } catch (error) {
            console.error('[UPLOAD] 이미지 업로드 실패:', error);
            callback('', '이미지 업로드 실패');
          }
        },
      },
    });

    editorRef.current.on('change', () => {
      if (editorRef.current) {
        const markdown = editorRef.current.getMarkdown();
        setFormData(prev => ({ ...prev, postBody: markdown }));
      }
    });

    return () => {
      if (editorRef.current) {
        editorRef.current.destroy();
        editorRef.current = null;
      }
    };
  }, []);

  const handleThumbnailChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    try {
      console.log('[UPLOAD] 썸네일 업로드 시작:', file.name);
      const url = await uploadImage(file);
      console.log('[UPLOAD] 썸네일 업로드 완료:', url);
      setFormData(prev => ({ ...prev, thumbnail: file, thumbnailUrl: url }));
    } catch (error) {
      console.error('[UPLOAD] 썸네일 업로드 실패:', error);
      alert('썸네일 업로드에 실패했습니다.');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCreating(true);
    setResult(null);

    try {
      const requestData = {
        title: formData.title,
        description: formData.description,
        category: formData.category,
        price: formData.type === 'paid' && formData.paymentType === 'one-time' ? parseInt(formData.price.replace(/,/g, '')) : null,
        planId: formData.type === 'paid' && formData.paymentType === 'subscription' ? parseInt(formData.planId) : null,
        thumbnailUrl: formData.thumbnailUrl || null,
        post: formData.postBody ? {
          body: formData.postBody,
          postFiles: []
        } : null
      };

      console.log('[POST] 콘텐츠 생성 요청:', requestData);

      const data = await createContent(requestData);
      console.log('[POST] 콘텐츠 생성 완료:', data);

      setResult({ success: true, data });
      alert(`콘텐츠 생성 성공! ID: ${data.contentId}`);
      router.push(`/test/content/${data.contentId}/view`);
    } catch (err) {
      console.error('[POST] 콘텐츠 생성 실패:', err);
      setResult({ success: false, error: err.message });
    } finally {
      setCreating(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold mb-6">콘텐츠 생성 테스트</h1>

        {/* 보낼 DTO JSON 표시 */}
        <div className="bg-white rounded-lg border border-gray-200 p-6 mb-6">
          <h2 className="text-xl font-semibold mb-4">보낼 DTO (JSON)</h2>
          <pre className="bg-gray-100 p-4 rounded-lg overflow-auto text-xs">
            {JSON.stringify(getRequestDto(), null, 2)}
          </pre>
        </div>

        <form onSubmit={handleSubmit} className="bg-white rounded-lg border border-gray-200 p-6 space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              제목 *
            </label>
            <input
              type="text"
              value={formData.title}
              onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              설명 *
            </label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
              required
              rows={3}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              카테고리 *
            </label>
            <select
              value={formData.category}
              onChange={(e) => setFormData(prev => ({ ...prev, category: e.target.value }))}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
            >
              {categories.map((cat) => (
                <option key={cat.code} value={cat.code}>
                  {cat.label}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              타입
            </label>
            <div className="flex gap-4">
              <label className="flex items-center">
                <input
                  type="radio"
                  value="free"
                  checked={formData.type === 'free'}
                  onChange={(e) => setFormData(prev => ({ ...prev, type: e.target.value }))}
                  className="mr-2"
                />
                무료
              </label>
              <label className="flex items-center">
                <input
                  type="radio"
                  value="paid"
                  checked={formData.type === 'paid'}
                  onChange={(e) => setFormData(prev => ({ ...prev, type: e.target.value }))}
                  className="mr-2"
                />
                유료
              </label>
            </div>
          </div>

          {formData.type === 'paid' && (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                결제 방식
              </label>
              <div className="flex gap-4">
                <label className="flex items-center">
                  <input
                    type="radio"
                    value="subscription"
                    checked={formData.paymentType === 'subscription'}
                    onChange={(e) => setFormData(prev => ({ ...prev, paymentType: e.target.value }))}
                    className="mr-2"
                  />
                  구독
                </label>
                <label className="flex items-center">
                  <input
                    type="radio"
                    value="one-time"
                    checked={formData.paymentType === 'one-time'}
                    onChange={(e) => setFormData(prev => ({ ...prev, paymentType: e.target.value }))}
                    className="mr-2"
                  />
                  단건결제
                </label>
              </div>
            </div>
          )}

          {formData.type === 'paid' && formData.paymentType === 'one-time' && (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                가격
              </label>
              <input
                type="text"
                value={formData.price}
                onChange={(e) => setFormData(prev => ({ ...prev, price: e.target.value.replace(/\D/g, '') }))}
                placeholder="숫자만 입력"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
              />
            </div>
          )}

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              썸네일
            </label>
            <input
              ref={thumbnailInputRef}
              type="file"
              accept="image/*"
              onChange={handleThumbnailChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
            />
            {formData.thumbnailUrl && (
              <img src={formData.thumbnailUrl} alt="썸네일 미리보기" className="mt-2 max-w-xs h-auto rounded" />
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              본문 (ToastUI Editor)
            </label>
            <div className="toastui-editor-wrapper">
              <div ref={editorDivRef} />
            </div>
          </div>

          <div className="flex gap-4">
            <button
              type="submit"
              disabled={creating}
              className="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed font-semibold"
            >
              {creating ? '생성 중...' : '콘텐츠 생성'}
            </button>
            <button
              type="button"
              onClick={() => router.push('/test/content')}
              className="px-6 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 font-semibold"
            >
              취소
            </button>
          </div>
        </form>

        {result && (
          <div className={`mt-6 bg-white rounded-lg border p-6 ${result.success ? 'border-green-200 bg-green-50' : 'border-red-200 bg-red-50'}`}>
            <h3 className="text-lg font-semibold mb-2">{result.success ? '✅ 성공' : '❌ 실패'}</h3>
            {result.success ? (
              <pre className="text-sm overflow-auto">{JSON.stringify(result.data, null, 2)}</pre>
            ) : (
              <p className="text-red-600">{result.error}</p>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

