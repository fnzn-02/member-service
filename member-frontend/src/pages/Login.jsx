import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api/api';

const Login = ({ setIsAuthenticated }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/api/members/login', { email, password });
      localStorage.setItem('token', response.data.accessToken);
      setIsAuthenticated(true);
      navigate('/mypage');
    } catch (error) {
      const errorMessage = error.response?.data?.errorMessage || '로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.';
      alert(errorMessage);
    }
  };

  return (
    <div className="w-full flex justify-center items-center animate-fade-in-up mt-10">
      <div className="w-full max-w-md bg-white dark:bg-netflixCard p-10 rounded-[2.5rem] shadow-[0_10px_40px_rgba(0,0,0,0.08)] dark:shadow-none border border-gray-100 dark:border-gray-800 transition-all duration-300">
        <h2 className="text-3xl font-extrabold text-center mb-10 tracking-tight">
          <span className="text-tossBlue">ONAIR</span> 로그인
        </h2>
        <form onSubmit={handleLogin} className="space-y-6">
          <div>
            <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">이메일</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue focus:bg-white dark:focus:bg-netflixBlack focus:ring-4 focus:ring-tossBlue/20 transition-all outline-none dark:text-white font-medium placeholder-gray-400"
              placeholder="onair@example.com"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">비밀번호</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue focus:bg-white dark:focus:bg-netflixBlack focus:ring-4 focus:ring-tossBlue/20 transition-all outline-none dark:text-white font-medium placeholder-gray-400"
              placeholder="비밀번호를 입력해주세요"
              required
            />
          </div>
          <button
            type="submit"
            className="w-full py-4 mt-4 bg-tossBlue hover:bg-tossBlueHover text-white text-lg font-bold rounded-2xl shadow-lg shadow-tossBlue/30 hover:-translate-y-1 transition-all duration-200"
          >
            로그인하기
          </button>
        </form>
        <div className="mt-8 space-y-3 text-center text-sm text-gray-500 dark:text-gray-400">
          <div>
            <Link to="/forgot-password" className="hover:text-tossBlue transition-colors">
              비밀번호를 잊으셨나요?
            </Link>
          </div>
          <div>
            아직 계정이 없으신가요?{' '}
            <Link to="/signup" className="text-tossBlue font-bold hover:underline transition-all">
              회원가입
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
