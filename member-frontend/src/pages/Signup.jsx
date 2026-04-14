import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api/api';

const Signup = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [nickname, setNickname] = useState('');
  const [code, setCode] = useState('');
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({});

  const navigate = useNavigate();

  const handleSendCode = async () => {
    if (!email) return alert('이메일을 입력해주세요!');
    setIsLoading(true);
    setErrors({});
    try {
      await api.post(`/api/members/signup/code?email=${email}`);
      alert(isCodeSent ? '인증번호가 재발송되었습니다.' : '인증번호가 발송되었습니다.');
      setIsCodeSent(true);
    } catch (error) {
      alert(error.response?.data?.errorMessage || '이미 가입된 이메일이거나 발송에 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleVerifyCode = async () => {
    if (code.length !== 6) return alert('인증번호 6자리를 입력해주세요.');
    try {
      await api.post(`/api/members/signup/verify?email=${email}&code=${code}`);
      alert('이메일 인증이 완료되었습니다!');
      setIsEmailVerified(true);
      setErrors({});
    } catch (error) {
      alert(error.response?.data?.errorMessage || '인증번호가 일치하지 않습니다.');
    }
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    setErrors({});

    if (!isEmailVerified) return alert('먼저 이메일 인증을 완료해주세요!');
    if (password !== passwordConfirm) {
      setErrors({ passwordConfirm: '비밀번호가 서로 일치하지 않습니다.' });
      return;
    }

    try {
      await api.post('/api/members/signup', { email, password, nickname, code });
      alert('ONAIR 멤버가 되신 것을 환영합니다!');
      navigate('/login');
    } catch (error) {
      if (error.response?.status === 400) {
        setErrors(error.response.data);
      } else {
        alert('서버와 연결할 수 없습니다.');
      }
    }
  };

  return (
    <div className="w-full flex justify-center items-center animate-fade-in-up mt-6 mb-10">
      <div className="w-full max-w-md bg-white dark:bg-netflixCard p-10 rounded-[2.5rem] shadow-[0_10px_40px_rgba(0,0,0,0.08)] dark:shadow-none border border-gray-100 dark:border-gray-800 transition-all duration-300">
        <h2 className="text-3xl font-extrabold tracking-tight text-center mb-10">
          <span className="text-tossBlue">ONAIR</span> 합류하기
        </h2>
        <form onSubmit={handleSignup} className="space-y-5">
          <div>
            <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">이메일</label>
            <div className="flex gap-2">
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                readOnly={isEmailVerified}
                className={`w-full px-5 py-4 rounded-2xl border transition-all outline-none dark:text-white font-medium ${
                  errors.email
                    ? 'border-red-500 bg-red-50 dark:bg-red-900/10'
                    : isEmailVerified
                    ? 'bg-green-50 dark:bg-green-900/10 border-green-500'
                    : 'bg-gray-50 dark:bg-netflixBlack border-transparent focus:border-tossBlue'
                }`}
                placeholder="onair@example.com"
              />
              <button
                type="button"
                onClick={handleSendCode}
                disabled={isLoading || isEmailVerified}
                className="whitespace-nowrap px-4 py-4 rounded-2xl bg-gray-900 dark:bg-white text-white dark:text-gray-900 font-bold hover:brightness-110 disabled:opacity-50 transition-all"
              >
                {isCodeSent ? '재발송' : '인증 받기'}
              </button>
            </div>
            {errors.email && <p className="text-red-500 text-xs mt-2 pl-2 font-semibold">{errors.email}</p>}
          </div>

          {isCodeSent && !isEmailVerified && (
            <div className="animate-fade-in-up">
              <label className="block text-sm font-semibold text-tossBlue mb-2 pl-1">인증번호 6자리</label>
              <div className="flex gap-2">
                <input
                  type="text"
                  value={code}
                  onChange={(e) => setCode(e.target.value)}
                  maxLength={6}
                  className="w-full px-5 py-4 rounded-2xl bg-blue-50/50 dark:bg-blue-900/20 border border-tossBlue/30 focus:border-tossBlue transition-all outline-none dark:text-white font-bold tracking-widest text-center text-lg"
                  placeholder="000000"
                />
                <button
                  type="button"
                  onClick={handleVerifyCode}
                  className="whitespace-nowrap px-6 py-4 rounded-2xl bg-tossBlue text-white font-bold hover:bg-tossBlueHover transition-all shadow-md shadow-tossBlue/20"
                >
                  인증 확인
                </button>
              </div>
            </div>
          )}

          <div className="space-y-5">
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">닉네임</label>
              <input
                type="text"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                className={`w-full px-5 py-4 rounded-2xl transition-all outline-none dark:text-white font-medium ${
                  errors.nickname
                    ? 'border-red-500 bg-red-50 dark:bg-red-900/10 border'
                    : 'bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue'
                }`}
                placeholder="닉네임"
              />
              {errors.nickname && <p className="text-red-500 text-xs mt-2 pl-2 font-semibold">{errors.nickname}</p>}
            </div>
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">비밀번호</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className={`w-full px-5 py-4 rounded-2xl transition-all outline-none dark:text-white font-medium ${
                  errors.password
                    ? 'border-red-500 bg-red-50 dark:bg-red-900/10 border'
                    : 'bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue'
                }`}
                placeholder="특수문자 포함 8자 이상"
              />
              {errors.password && <p className="text-red-500 text-xs mt-2 pl-2 font-semibold">{errors.password}</p>}
            </div>
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">비밀번호 확인</label>
              <input
                type="password"
                value={passwordConfirm}
                onChange={(e) => setPasswordConfirm(e.target.value)}
                className={`w-full px-5 py-4 rounded-2xl transition-all outline-none dark:text-white font-medium ${
                  errors.passwordConfirm
                    ? 'border-red-500 bg-red-50 dark:bg-red-900/10 border'
                    : 'border border-transparent bg-gray-50 dark:bg-netflixBlack focus:border-tossBlue'
                }`}
                placeholder="한 번 더 입력"
              />
              {errors.passwordConfirm && <p className="text-red-500 text-xs mt-2 pl-2 font-semibold">{errors.passwordConfirm}</p>}
            </div>
          </div>

          <button
            type="submit"
            disabled={!isEmailVerified}
            className={`w-full py-4 mt-8 text-white text-lg font-bold rounded-2xl transition-all duration-200 shadow-lg ${
              isEmailVerified
                ? 'bg-tossBlue hover:bg-tossBlueHover shadow-tossBlue/30 hover:-translate-y-1'
                : 'bg-gray-300 cursor-not-allowed shadow-none'
            }`}
          >
            가입 완료하기
          </button>
        </form>
        <div className="mt-8 text-center text-sm text-gray-500 dark:text-gray-400">
          이미 계정이 있으신가요?{' '}
          <Link to="/login" className="text-tossBlue font-bold hover:underline transition-all">
            로그인
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Signup;
