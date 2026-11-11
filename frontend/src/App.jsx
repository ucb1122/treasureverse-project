import { useEffect, useMemo, useState } from "react";
import {
  createSalePost,
  getPurchasePosts,
  getSalePosts,
  login,
  signup,
  uploadSalePostImage,
} from "./api";

const categories = [
  { label: "트레이딩 카드", icon: "♠", tone: "violet" },
  { label: "피규어", icon: "◉", tone: "orange" },
  { label: "빈티지", icon: "◈", tone: "green" },
  { label: "아트 토이", icon: "✦", tone: "blue" },
  { label: "음반 · 굿즈", icon: "♫", tone: "red" },
  { label: "전시회", icon: "▤", tone: "yellow" },
];

const showcaseItems = [
  {
    id: "m1",
    title: "1998 포켓몬 카드 컬렉션",
    price: 380000,
    type: "판매",
    accent: "violet",
    tag: "인기",
  },
  {
    id: "m2",
    title: "빈티지 필름 카메라 세트",
    price: 245000,
    type: "판매",
    accent: "orange",
    tag: "오늘 마감",
  },
  {
    id: "m3",
    title: "한정판 아트 토이",
    price: 160000,
    type: "구매 희망",
    accent: "green",
    tag: "NEW",
  },
  {
    id: "m4",
    title: "LP 초판 앨범을 찾습니다",
    price: 120000,
    type: "구매 희망",
    accent: "blue",
    tag: "문의 12",
  },
];

const starField = Array.from({ length: 26 }, (_, index) => index);

const formatPrice = (price) =>
  new Intl.NumberFormat("ko-KR").format(price ?? 0);

function ListingCard({ item, isFallback }) {
  const isPurchase = item.type === "구매 희망";
  return (
    <article className="listing-card">
      <div className={`listing-art ${item.accent ?? "violet"}`}>
        <span className="listing-tag">
          {item.tag ?? (isPurchase ? "구매 희망" : "판매 중")}
        </span>
        <span className="listing-gridline" />
        <span className="artifact">{isPurchase ? "◎" : "◇"}</span>
        <span className="listing-orbit" />
      </div>
      <div className="listing-copy">
        <span className={isPurchase ? "post-type wanted" : "post-type"}>
          {item.type ?? "판매"}
        </span>
        <h3>{item.title}</h3>
        <strong>{formatPrice(item.price ?? item.desiredPrice)}원</strong>
        <p>
          {isFallback
            ? "예시 컬렉션 · 백엔드 연결 후 실제 게시글로 교체됩니다."
            : "Treasureverse 등록 게시글"}
        </p>
      </div>
    </article>
  );
}

function LoginPanel({ onClose, onLogin, onSignup }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      const token = await login(username, password);
      localStorage.setItem("treasureverse.accessToken", token.accessToken);
      onLogin(username);
      onClose();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="modal-backdrop" role="presentation" onMouseDown={onClose}>
      <form
        className="login-panel"
        onSubmit={submit}
        onMouseDown={(event) => event.stopPropagation()}
      >
        <button
          className="close-button"
          type="button"
          onClick={onClose}
          aria-label="로그인 창 닫기"
        >
          ×
        </button>
        <span className="eyebrow">WELCOME BACK</span>
        <h2>나의 보물을 이어가세요.</h2>
        <label>
          아이디
          <input
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            required
          />
        </label>
        <label>
          비밀번호
          <input
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </label>
        {error && <p className="form-error">{error}</p>}
        <button className="primary-button" disabled={loading}>
          {loading ? "로그인 중…" : "로그인"}
        </button>
        <p className="form-note">
          계정이 없나요?{" "}
          <button className="inline-button" type="button" onClick={onSignup}>
            회원가입
          </button>
        </p>
      </form>
    </div>
  );
}

function SignupPanel({ onClose, onComplete }) {
  const [form, setForm] = useState({
    username: "",
    password: "",
    name: "",
    phoneNumber: "",
    postalCode: "",
    address: "",
    addressDetail: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const update = (event) =>
    setForm((previous) => ({
      ...previous,
      [event.target.name]: event.target.value,
    }));

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      await signup(form);
      onComplete(form.username);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="modal-backdrop" role="presentation" onMouseDown={onClose}>
      <form
        className="login-panel signup-panel"
        onSubmit={submit}
        onMouseDown={(event) => event.stopPropagation()}
      >
        <button
          className="close-button"
          type="button"
          onClick={onClose}
          aria-label="회원가입 창 닫기"
        >
          ×
        </button>
        <span className="eyebrow">JOIN TREASUREVERSE</span>
        <h2>
          첫 번째 보물을
          <br />
          등록할 준비를 하세요.
        </h2>
        <div className="form-grid">
          <label>
            아이디
            <input
              name="username"
              value={form.username}
              onChange={update}
              pattern="[a-zA-Z0-9_]{4,30}"
              required
            />
          </label>
          <label>
            이름
            <input name="name" value={form.name} onChange={update} required />
          </label>
          <label>
            비밀번호
            <input
              name="password"
              type="password"
              minLength="8"
              value={form.password}
              onChange={update}
              required
            />
          </label>
          <label>
            전화번호
            <input
              name="phoneNumber"
              value={form.phoneNumber}
              onChange={update}
              placeholder="010-1234-5678"
              required
            />
          </label>
          <label>
            우편번호
            <input
              name="postalCode"
              value={form.postalCode}
              onChange={update}
            />
          </label>
          <label>
            주소
            <input
              name="address"
              value={form.address}
              onChange={update}
              required
            />
          </label>
        </div>
        <label>
          상세 주소
          <input
            name="addressDetail"
            value={form.addressDetail}
            onChange={update}
          />
        </label>
        {error && <p className="form-error">{error}</p>}
        <button className="primary-button" disabled={loading}>
          {loading ? "가입 중…" : "회원가입"}
        </button>
      </form>
    </div>
  );
}

function SellPanel({ onClose, onCreated }) {
  const [form, setForm] = useState({ title: "", content: "", price: "" });
  const [file, setFile] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const update = (event) =>
    setForm((previous) => ({
      ...previous,
      [event.target.name]: event.target.value,
    }));

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      const salePost = await createSalePost({
        ...form,
        price: Number(form.price),
      });
      if (file) await uploadSalePostImage(salePost.id, file);
      onCreated({ ...salePost, type: "판매" });
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="modal-backdrop" role="presentation" onMouseDown={onClose}>
      <form
        className="login-panel sell-panel"
        onSubmit={submit}
        onMouseDown={(event) => event.stopPropagation()}
      >
        <button
          className="close-button"
          type="button"
          onClick={onClose}
          aria-label="판매 등록 창 닫기"
        >
          ×
        </button>
        <span className="eyebrow">SELL A TREASURE</span>
        <h2>
          새로운 컬렉터에게
          <br />
          보물을 소개하세요.
        </h2>
        <label>
          제목
          <input
            name="title"
            maxLength="100"
            value={form.title}
            onChange={update}
            required
          />
        </label>
        <label>
          설명
          <textarea
            name="content"
            value={form.content}
            onChange={update}
            required
          />
        </label>
        <label>
          판매 가격
          <input
            name="price"
            type="number"
            min="1"
            value={form.price}
            onChange={update}
            required
          />
        </label>
        <label>
          대표 이미지{" "}
          <input
            type="file"
            accept="image/*"
            onChange={(event) => setFile(event.target.files?.[0] ?? null)}
          />
        </label>
        {file && (
          <p className="file-note">
            {file.name} · {(file.size / 1024 / 1024).toFixed(1)} MB
          </p>
        )}
        {error && <p className="form-error">{error}</p>}
        <button className="primary-button" disabled={loading}>
          {loading ? "등록 중…" : "판매글 등록"}
        </button>
      </form>
    </div>
  );
}

export default function App() {
  const [salePosts, setSalePosts] = useState([]);
  const [purchasePosts, setPurchasePosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [apiError, setApiError] = useState("");
  const [query, setQuery] = useState("");
  const [loginOpen, setLoginOpen] = useState(false);
  const [signupOpen, setSignupOpen] = useState(false);
  const [sellOpen, setSellOpen] = useState(false);
  const [memberName, setMemberName] = useState(
    localStorage.getItem("treasureverse.memberName") ?? "",
  );

  useEffect(() => {
    Promise.all([getSalePosts(), getPurchasePosts()])
      .then(([sales, purchases]) => {
        setSalePosts(sales.map((item) => ({ ...item, type: "판매" })));
        setPurchasePosts(
          purchases.map((item) => ({ ...item, type: "구매 희망" })),
        );
      })
      .catch((error) => setApiError(error.message))
      .finally(() => setLoading(false));
  }, []);

  const listings = useMemo(
    () => [...salePosts, ...purchasePosts],
    [salePosts, purchasePosts],
  );
  const filteredListings = listings.filter((item) =>
    item.title.toLowerCase().includes(query.toLowerCase()),
  );
  const displayListings =
    apiError || listings.length === 0 ? showcaseItems : filteredListings;

  function handleLogin(username) {
    localStorage.setItem("treasureverse.memberName", username);
    setMemberName(username);
  }

  function handleSignupComplete(username) {
    setSignupOpen(false);
    setLoginOpen(true);
    setMemberName("");
    localStorage.removeItem("treasureverse.memberName");
    window.alert(`${username}님, 회원가입이 완료되었습니다. 로그인해 주세요.`);
  }

  function handleSaleCreated(salePost) {
    setSalePosts((previous) => [salePost, ...previous]);
    setSellOpen(false);
  }

  return (
    <div className="app-shell">
      <header className="site-header">
        <a className="wordmark" href="#top" aria-label="Treasureverse 홈">
          TREASURE<span>VERSE</span>
        </a>
        <nav aria-label="주요 탐색">
          <a href="#market">마켓</a>
          <a href="#collect">컬렉션</a>
          <a href="#exhibit">전시회</a>
          <a href="#auction">경매</a>
        </nav>
        <div className="header-actions">
          {memberName ? (
            <span className="member-chip">{memberName} 님</span>
          ) : (
            <button className="text-button" onClick={() => setLoginOpen(true)}>
              로그인
            </button>
          )}
          <button
            className="sell-button"
            onClick={() =>
              memberName ? setSellOpen(true) : setLoginOpen(true)
            }
          >
            판매하기 <span>↗</span>
          </button>
        </div>
      </header>

      <main id="top">
        <section className="hero">
          <div className="hero-stars" aria-hidden="true">
            {starField.map((star) => (
              <i key={star} style={{ "--star": star }} />
            ))}
          </div>
          <div className="hero-copy">
            <span className="eyebrow">
              <b /> COLLECTOR'S ORBIT · 2026
            </span>
            <h1>
              당신의 보물이
              <br />
              <em>우주를 건너</em>
              <br />
              빛나는 곳.
            </h1>
            <p>
              희귀한 취향은 별처럼 흩어지지 않도록.
              <br />
              발견하고, 기록하고, 다음 컬렉터에게 이어주세요.
            </p>
            <div className="search-box">
              <span>⌕</span>
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="찾고 있는 수집품을 검색하세요"
                aria-label="수집품 검색"
              />
              <button>검색</button>
            </div>
            <div className="hero-stats">
              <span>
                <b>10K+</b> 발견된 보물
              </span>
              <span>
                <b>24H</b> 실시간 탐색
              </span>
            </div>
          </div>
          <div className="hero-art" aria-label="컬렉션 오브제 일러스트">
            <div className="nebula nebula-one" />
            <div className="nebula nebula-two" />
            <div className="planet planet-one">
              <span />
            </div>
            <div className="planet planet-two" />
            <div className="orbit-line orbit-line-one" />
            <div className="orbit-line orbit-line-two" />
            <div className="hero-object">
              <span className="object-glow" />◇
            </div>
            <div className="signal-card">
              <span className="signal-dot" /> RARE FIND
              <br />
              <b>01 · 094</b>
            </div>
            <span className="orbit orbit-one">DISCOVER · KEEP · TRADE</span>
            <span className="orbit orbit-two">TREASUREVERSE</span>
          </div>
        </section>

        <section className="category-section" id="collect">
          <div className="section-heading">
            <div>
              <span className="eyebrow">EXPLORE BY TYPE</span>
              <h2>무엇을 찾고 있나요?</h2>
            </div>
            <a href="#market">전체 카테고리 보기 →</a>
          </div>
          <div className="category-grid">
            {categories.map((category) => (
              <button className="category-card" key={category.label}>
                <span className={`category-icon ${category.tone}`}>
                  {category.icon}
                </span>
                <span>{category.label}</span>
                <small>둘러보기 →</small>
              </button>
            ))}
          </div>
        </section>

        <section className="market-section" id="market">
          <div className="section-heading">
            <div>
              <span className="eyebrow">CURATED MARKET</span>
              <h2>지금 주목받는 보물</h2>
            </div>
            <div className="listing-tabs">
              <button className="active">전체</button>
              <button>판매</button>
              <button>구매 희망</button>
            </div>
          </div>
          {loading ? (
            <div className="loading-state">컬렉션을 불러오는 중입니다…</div>
          ) : (
            <>
              <div className="listing-grid">
                {displayListings.slice(0, 8).map((item) => (
                  <ListingCard
                    item={item}
                    key={item.id}
                    isFallback={Boolean(apiError || listings.length === 0)}
                  />
                ))}
              </div>
              {apiError && (
                <p className="api-notice">
                  백엔드에 연결하지 못해 예시 컬렉션을 보여주고 있습니다.
                  백엔드를 8080 포트에서 실행한 뒤 새로고침해 보세요.
                </p>
              )}
            </>
          )}
        </section>

        <section className="story-banner" id="exhibit">
          <div className="banner-stars" aria-hidden="true" />{" "}
          <div>
            <span className="eyebrow">TREASUREVERSE EXHIBITION</span>
            <h2>
              취향이 모이면
              <br />
              하나의 <em>은하</em>가 됩니다.
            </h2>
            <a href="#exhibit" className="light-link">
              전시회 둘러보기 <span>→</span>
            </a>
          </div>
          <div className="banner-orbit">
            <div className="banner-mark">
              TV
              <br />
              <span>2026</span>
            </div>
          </div>
        </section>
      </main>

      <footer>
        <a className="wordmark" href="#top">
          TREASURE<span>VERSE</span>
        </a>
        <p>Collect what moves you.</p>
        <span>© 2026 Treasureverse</span>
      </footer>
      {loginOpen && (
        <LoginPanel
          onClose={() => setLoginOpen(false)}
          onLogin={handleLogin}
          onSignup={() => {
            setLoginOpen(false);
            setSignupOpen(true);
          }}
        />
      )}
      {signupOpen && (
        <SignupPanel
          onClose={() => setSignupOpen(false)}
          onComplete={handleSignupComplete}
        />
      )}
      {sellOpen && (
        <SellPanel
          onClose={() => setSellOpen(false)}
          onCreated={handleSaleCreated}
        />
      )}
    </div>
  );
}
