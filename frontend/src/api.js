const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api/v1";

async function request(path, options = {}) {
  const token = localStorage.getItem("treasureverse.accessToken");
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  });

  if (!response.ok) {
    const error = await response.json().catch(() => null);
    throw new Error(error?.message ?? "요청을 처리하지 못했습니다.");
  }

  if (response.status === 204) return null;
  const payload = await response.json();
  return payload.data;
}

export function getSalePosts() {
  return request("/sale-posts");
}

export function getPurchasePosts() {
  return request("/purchase-posts");
}

export function login(username, password) {
  return request("/auth/login", {
    method: "POST",
    body: JSON.stringify({ username, password }),
  });
}

export function signup(payload) {
  return request("/auth/signup", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function createSalePost(payload) {
  return request("/sale-posts", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function uploadSalePostImage(salePostId, file) {
  const { key, uploadUrl } = await request("/images/presign", {
    method: "POST",
    body: JSON.stringify({ contentType: file.type, size: file.size }),
  });

  const uploadResponse = await fetch(uploadUrl, {
    method: "PUT",
    headers: { "Content-Type": file.type },
    body: file,
  });

  if (!uploadResponse.ok) {
    throw new Error("이미지를 저장하지 못했습니다. S3 설정을 확인해 주세요.");
  }

  return request(`/sale-posts/${salePostId}/images`, {
    method: "POST",
    body: JSON.stringify({ objectKey: key }),
  });
}
