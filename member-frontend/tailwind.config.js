/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}", // src 폴더 안의 모든 리액트 파일에서 디자인을 쓰겠다!
  ],
  darkMode: 'class', // 수동으로 다크모드 껐다 켜는 기능
  theme: {
    extend: {
      fontFamily: {
        // 프리텐다드 폰트를 최우선으로 적용
        sans: ['Pretendard', '-apple-system', 'BlinkMacSystemFont', 'system-ui', 'Roboto', 'sans-serif'],
      },
      colors: {
        tossBlue: '#3182f6',
        tossBlueHover: '#1b64da',
        netflixBlack: '#141414',
        netflixCard: '#2b2b2b',
      },
      animation: {
        // 화면 켜질 때 스르륵 나타나는 고급 애니메이션
        'fade-in-up': 'fadeInUp 0.6s cubic-bezier(0.16, 1, 0.3, 1) forwards',
      },
      keyframes: {
        fadeInUp: {
          '0%': { opacity: '0', transform: 'translateY(20px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        }
      }
    },
  },
  plugins: [],
}