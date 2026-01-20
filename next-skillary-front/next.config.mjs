/** @type {import('next').NextConfig} */
const nextConfig = {
  output: 'standalone', // Docker 빌드를 위한 standalone 출력 모드
  eslint: {
    ignoreDuringBuilds: true,
  },
  typescript: {
    ignoreBuildErrors: true,
  },
  // 기존 설정들...
};

// module.exports 대신 아래 코드를 사용하세요
export default nextConfig;