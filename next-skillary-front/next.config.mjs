/** @type {import('next').NextConfig} */
const nextConfig = {
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