import { useEffect, useMemo, useState } from "react";
import {
  createSalePost,
  getPurchasePosts,
  getSalePost,
  getSalePosts,
  login,
  signup,
  uploadSalePostImage,
} from "./api";

const categories = [
  "트레이딩 카드",
  "피규어",
  "빈티지",
  "아트 토이",
  "음반 · 굿즈",
  "전시회",
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
    tag: "신규",
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

const formatPrice = (price) =>
  new Intl.NumberFormat("ko-KR").format(price ?? 0);

function ListingCard({ item, isFallback, onSelect }) {
  const isPurchase = item.type === "구매 희망";
  return (
    <article className="listing-card" onClick={() => onSelect(item)}>
      <div className="listing-art">
        {item.imageUrls?.[0] && (
          <img
            className="listing-image"
            src={item.imageUrls[0]}
            alt={item.title}
          />
        )}
        <span className="listing-tag">
          {item.tag ?? (isPurchase ? "구매 희망" : "판매 중")}
        </span>
        {!item.imageUrls?.[0] && (
          <span className="placeholder-image">상품 이미지</span>
        )}
      </div>
      <div className="listing-copy">
        <span className={isPurchase ? "post-type wanted" : "post-type"}>
          {item.type ?? "판매"}
        </span>
        <h3>{item.title}</h3>
        <strong>{formatPrice(item.price ?? item.desiredPrice)}원</strong>
        <p>{isFallback ? "예시 게시글" : "안전 결제 지원"}</p>
      </div>
    </article>
  );
}

function ListingDetail({ item, onBack }) {
  return (
    <main className="detail-page">
      <header className="detail-header">
        <a
          className="brand-logo"
          href="/"
          onClick={(event) => {
            event.preventDefault();
            onBack();
          }}
        >
          <img src="/brand/treasureverse-logo.png" alt="Treasureverse" />
        </a>
        <button className="back-button" type="button" onClick={onBack}>
          ← 마켓으로 돌아가기
        </button>
      </header>
      <section className="listing-detail detail-hero">
        <div className="detail-gallery" aria-label="판매 이미지">
          {item.imageUrls?.length ? (
            item.imageUrls.map((url) => (
              <img key={url} src={url} alt={item.title} />
            ))
          ) : (
            <div className="detail-placeholder">등록된 이미지가 없습니다</div>
          )}
        </div>
        <div className="detail-copy">
          <span className="eyebrow">{item.type ?? "판매"}</span>
          <h2>{item.title}</h2>
          <strong>{formatPrice(item.price ?? item.desiredPrice)}원</strong>
          <p className="detail-meta">
            판매자와 메시지로 거래 조건을 확인하세요.
          </p>
          <button className="primary-button detail-action" type="button">
            문의하기
          </button>
        </div>
      </section>
      <section className="detail-description">
        <span className="eyebrow">상품 설명</span>
        <h3>판매자 설명</h3>
        <p>{item.content ?? "등록된 상세 설명이 없습니다."}</p>
      </section>
    </main>
  );
}

function ErrorModal({ message, onClose }) {
  return (
    <div className="modal-backdrop" role="presentation" onMouseDown={onClose}>
      <section
        className="error-modal"
        role="alertdialog"
        onMouseDown={(event) => event.stopPropagation()}
      >
        <span className="error-icon">!</span>
        <h2>요청을 완료하지 못했습니다</h2>
        <p>{message || "잠시 후 다시 시도해 주세요."}</p>
        <button className="primary-button" type="button" onClick={onClose}>
          확인
        </button>
      </section>
    </div>
  );
}

function LoginPanel({ onClose, onLogin, onSignup, onError }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    try {
      const token = await login(username, password);
      localStorage.setItem("treasureverse.accessToken", token.accessToken);
      onLogin(username);
      onClose();
    } catch (requestError) {
      onError(requestError.message);
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
        <span className="eyebrow">로그인</span>
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

function SignupPanel({ onClose, onComplete, onError }) {
  const [form, setForm] = useState({
    username: "",
    password: "",
    name: "",
    phoneNumber: "",
    postalCode: "",
    address: "",
    addressDetail: "",
  });
  const [loading, setLoading] = useState(false);
  const update = (event) =>
    setForm((previous) => ({
      ...previous,
      [event.target.name]: event.target.value,
    }));

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    try {
      await signup(form);
      onComplete(form.username);
    } catch (requestError) {
      onError(requestError.message);
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
        <span className="eyebrow">회원가입</span>
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
              pattern="[a-zA-Z0-9._@-]{4,30}"
              title="4~30자의 영문, 숫자, 밑줄(_), 점(.), 하이픈(-), 골뱅이(@)만 사용할 수 있습니다."
              placeholder="아이디 또는 이메일 형태"
              required
            />
            <small className="field-hint">
              4~30자 · 예: collector_01 또는 name@example.com
            </small>
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
              title="비밀번호는 8자 이상 입력해 주세요."
              value={form.password}
              onChange={update}
              required
            />
            <small className="field-hint">8자 이상 입력해 주세요.</small>
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
        <button className="primary-button" disabled={loading}>
          {loading ? "가입 중…" : "회원가입"}
        </button>
      </form>
    </div>
  );
}

function SellPanel({ onClose, onCreated, onError }) {
  const [form, setForm] = useState({ title: "", content: "", price: "" });
  const [file, setFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const update = (event) =>
    setForm((previous) => ({
      ...previous,
      [event.target.name]: event.target.value,
    }));

  function selectImage(event) {
    const selectedFile = event.target.files?.[0] ?? null;
    setFile(selectedFile);
    setPreviewUrl(selectedFile ? URL.createObjectURL(selectedFile) : "");
  }

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    try {
      const salePost = await createSalePost({
        ...form,
        price: Number(form.price),
      });
      if (file) await uploadSalePostImage(salePost.id, file);
      onCreated({ ...salePost, type: "판매" });
    } catch (requestError) {
      onError(requestError.message);
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
        <span className="eyebrow">판매 등록</span>
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
          대표 이미지
          <input type="file" accept="image/*" onChange={selectImage} />
        </label>
        <div className="image-upload-preview">
          {previewUrl ? (
            <img src={previewUrl} alt="선택한 대표 이미지 미리보기" />
          ) : (
            <span>이미지를 선택하면 미리보기가 표시됩니다.</span>
          )}
        </div>
        {file && (
          <p className="file-note">
            {file.name} · {(file.size / 1024 / 1024).toFixed(1)} MB
          </p>
        )}
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
  const [selectedListing, setSelectedListing] = useState(null);
  const [currentPath, setCurrentPath] = useState(window.location.pathname);
  const [errorMessage, setErrorMessage] = useState("");
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
      .catch((error) => {
        setApiError(error.message);
        setErrorMessage(error.message);
      })
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    async function syncDetailRoute() {
      const match = window.location.pathname.match(/^\/sale\/(\d+)$/);
      if (!match) {
        setSelectedListing(null);
        setCurrentPath(window.location.pathname);
        return;
      }

      try {
        const salePost = await getSalePost(match[1]);
        setSelectedListing({ ...salePost, type: "판매" });
        setCurrentPath(window.location.pathname);
      } catch (error) {
        setErrorMessage(error.message);
        window.history.replaceState({}, "", "/");
        setSelectedListing(null);
        setCurrentPath("/");
      }
    }

    syncDetailRoute();
    window.addEventListener("popstate", syncDetailRoute);
    return () => window.removeEventListener("popstate", syncDetailRoute);
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

  function openListing(item) {
    setSelectedListing(item);
    window.history.pushState({ saleId: item.id }, "", `/sale/${item.id}`);
    setCurrentPath(`/sale/${item.id}`);
    window.scrollTo(0, 0);
  }

  function closeListing() {
    setSelectedListing(null);
    window.history.pushState({}, "", "/");
    setCurrentPath("/");
  }

  function navigate(event, path) {
    event.preventDefault();
    setSelectedListing(null);
    setCurrentPath(path);
    window.history.pushState({}, "", path);
    window.scrollTo(0, 0);
  }

  if (selectedListing)
    return (
      <div className="app-shell">
        <ListingDetail item={selectedListing} onBack={closeListing} />
        {errorMessage && (
          <ErrorModal
            message={errorMessage}
            onClose={() => setErrorMessage("")}
          />
        )}
      </div>
    );

  if (currentPath !== "/") {
    const page = {
      "/market": {
        title: "마켓",
        description: "판매 상품과 구매 희망글을 목적에 맞게 둘러보세요.",
      },
      "/market/sale": {
        title: "판매 게시판",
        description: "지금 판매 중인 수집품입니다.",
      },
      "/market/purchase": {
        title: "구매 게시판",
        description: "컬렉터가 찾고 있는 수집품입니다.",
      },
      "/collection": {
        title: "컬렉션",
        description: "나만의 수집 기록을 관리하는 공간입니다.",
      },
      "/exhibitions": {
        title: "전시회",
        description: "준비 중인 전시회 정보를 곧 만나볼 수 있습니다.",
      },
      "/auctions": {
        title: "경매",
        description: "준비 중인 경매 정보를 곧 만나볼 수 있습니다.",
      },
    }[currentPath] ?? { title: "페이지를 찾을 수 없습니다.", description: "" };

    return (
      <div className="app-shell">
        <header className="site-header">
          <a
            className="brand-logo"
            href="/"
            onClick={(event) => navigate(event, "/")}
          >
            <img src="/brand/treasureverse-logo.png" alt="Treasureverse" />
          </a>
          <nav aria-label="주요 탐색">
            <a href="/market" onClick={(event) => navigate(event, "/market")}>
              마켓
            </a>
            <a
              href="/collection"
              onClick={(event) => navigate(event, "/collection")}
            >
              컬렉션
            </a>
            <a
              href="/exhibitions"
              onClick={(event) => navigate(event, "/exhibitions")}
            >
              전시회
            </a>
            <a
              href="/auctions"
              onClick={(event) => navigate(event, "/auctions")}
            >
              경매
            </a>
          </nav>
          <div className="header-actions">
            <button
              className="sell-button"
              onClick={() =>
                memberName ? setSellOpen(true) : setLoginOpen(true)
              }
            >
              판매하기
            </button>
          </div>
        </header>
        <main className="subpage">
          <div className="subpage-heading">
            <h1>{page.title}</h1>
            <p>{page.description}</p>
          </div>
          {currentPath.startsWith("/market") && (
            <>
              <div className="board-links">
                <a
                  href="/market/sale"
                  className={currentPath === "/market/sale" ? "active" : ""}
                  onClick={(event) => navigate(event, "/market/sale")}
                >
                  판매 게시판
                </a>
                <a
                  href="/market/purchase"
                  className={currentPath === "/market/purchase" ? "active" : ""}
                  onClick={(event) => navigate(event, "/market/purchase")}
                >
                  구매 게시판
                </a>
              </div>
              {currentPath === "/market" ? (
                <div className="board-choice">
                  <button onClick={(event) => navigate(event, "/market/sale")}>
                    판매 상품 둘러보기
                  </button>
                  <button
                    onClick={(event) => navigate(event, "/market/purchase")}
                  >
                    구매 희망글 보기
                  </button>
                </div>
              ) : loading ? (
                <div className="loading-state">게시글을 불러오는 중입니다…</div>
              ) : (
                <div className="listing-grid">
                  {displayListings
                    .filter((item) =>
                      currentPath === "/market/sale"
                        ? item.type === "판매"
                        : item.type === "구매 희망",
                    )
                    .map((item) => (
                      <ListingCard
                        item={item}
                        key={item.id}
                        isFallback={Boolean(apiError || listings.length === 0)}
                        onSelect={openListing}
                      />
                    ))}
                </div>
              )}
            </>
          )}
          {!currentPath.startsWith("/market") && (
            <div className="empty-page">서비스 준비 중입니다.</div>
          )}
        </main>
        {sellOpen && (
          <SellPanel
            onClose={() => setSellOpen(false)}
            onCreated={handleSaleCreated}
            onError={setErrorMessage}
          />
        )}
        {errorMessage && (
          <ErrorModal
            message={errorMessage}
            onClose={() => setErrorMessage("")}
          />
        )}
      </div>
    );
  }

  return (
    <div className="app-shell">
      <header className="site-header">
        <a
          className="brand-logo"
          href="/"
          onClick={(event) => navigate(event, "/")}
          aria-label="Treasureverse 홈"
        >
          <img src="/brand/treasureverse-logo.png" alt="Treasureverse" />
        </a>
        <nav aria-label="주요 탐색">
          <a href="/market" onClick={(event) => navigate(event, "/market")}>
            마켓
          </a>
          <a
            href="/collection"
            onClick={(event) => navigate(event, "/collection")}
          >
            컬렉션
          </a>
          <a
            href="/exhibitions"
            onClick={(event) => navigate(event, "/exhibitions")}
          >
            전시회
          </a>
          <a href="/auctions" onClick={(event) => navigate(event, "/auctions")}>
            경매
          </a>
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
          <div className="hero-copy">
            <h1>
              좋아하는 수집품을
              <br />
              발견하고 거래하세요.
            </h1>
            <p>카드, 피규어, 빈티지, 굿즈까지. 나만의 컬렉션을 만나보세요.</p>
            <div className="search-box">
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="찾고 있는 수집품을 검색하세요"
                aria-label="수집품 검색"
              />
              <button type="button">검색</button>
            </div>
          </div>
          <div className="hero-visual" aria-hidden="true">
            <img src="/brand/treasureverse-logo.png" alt="" />
            <span>수집 · 거래 · 연결</span>
          </div>
        </section>

        <section className="category-section" id="collect">
          <div className="section-heading">
            <div>
              <h2>카테고리</h2>
            </div>
            <a href="#market">전체 카테고리 보기 →</a>
          </div>
          <div className="category-grid">
            {categories.map((category) => (
              <button className="category-card" key={category}>
                {category}
              </button>
            ))}
          </div>
        </section>

        <section className="market-section" id="market">
          <div className="section-heading">
            <div>
              <h2>새로 올라온 상품</h2>
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
                    onSelect={openListing}
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

        <section className="sell-banner" id="exhibit">
          <div>
            <h2>내 수집품을 판매해 보세요.</h2>
            <p>
              간단한 등록으로 관심 있는 컬렉터에게 상품을 소개할 수 있습니다.
            </p>
          </div>
          <button
            className="primary-button"
            type="button"
            onClick={() =>
              memberName ? setSellOpen(true) : setLoginOpen(true)
            }
          >
            판매글 등록하기
          </button>
        </section>
      </main>

      <footer>
        <a className="brand-logo" href="#top">
          <img src="/brand/treasureverse-logo.png" alt="Treasureverse" />
        </a>
        <p>수집하는 즐거움, 함께 나누는 가치</p>
        <span>© 2026 Treasureverse</span>
      </footer>
      {loginOpen && (
        <LoginPanel
          onClose={() => setLoginOpen(false)}
          onLogin={handleLogin}
          onError={setErrorMessage}
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
          onError={setErrorMessage}
        />
      )}
      {sellOpen && (
        <SellPanel
          onClose={() => setSellOpen(false)}
          onCreated={handleSaleCreated}
          onError={setErrorMessage}
        />
      )}
      {errorMessage && (
        <ErrorModal
          message={errorMessage}
          onClose={() => setErrorMessage("")}
        />
      )}
    </div>
  );
}
