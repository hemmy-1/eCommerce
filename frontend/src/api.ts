import Constants from 'expo-constants';

function getDefaultApiUrl() {
  const hostUri = Constants.expoConfig?.hostUri || Constants.manifest2?.extra?.expoClient?.hostUri;
  const host = hostUri?.split(':')[0];
  return `http://${host || 'localhost'}:8080`;
}

export const API_BASE_URL = process.env.EXPO_PUBLIC_API_URL || getDefaultApiUrl();

export type Product = { id: string; name: string; description?: string; price: number; imageUrls?: string[]; stockQuantity: number; categoryName?: string; productStatus?: string };
export type Category = { id: string; name: string; description?: string; status?: string };
export type User = { id: string; nickName: string; email: string; role: string };
export type CartItem = { productId: string; productName: string; unitPrice: number; quantity: number };
export type Cart = { customerId: string; items: CartItem[]; cartSubtotal: number };
export type WishlistItem = { wishlistId: string; productId: string; productName: string; price: number; addedAt: string };
export type OrderItem = { productId: string; productName: string; price: number; quantity: number; itemSubtotal: number };
export type Order = { orderId: string; subtotal: number; shippingFee: number; totalAmount: number; status: string; createdAt: string; items: OrderItem[] };
export type Payment = { paymentId: string; orderId: string; amount: number; status: string; transactionReference: string; paymentUrl: string; createdAt: string };

type RequestOptions = RequestInit & { token?: string };
async function request<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const { token, ...init } = options;
  const url = `${API_BASE_URL}${path}`;
  let response: Response;
  try {
    response = await fetch(url, { ...init, headers: { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}), ...init.headers } });
  } catch (error) {
    const reason = error instanceof Error ? error.message : 'Network request failed';
    throw new Error(`Cannot reach ${API_BASE_URL}. ${reason}`);
  }
  const text = await response.text();
  let body: unknown = null;
  try { body = text ? JSON.parse(text) : null; } catch { body = text; }
  if (!response.ok) throw new Error(typeof body === 'string' ? body : `Request failed (${response.status})`);
  return body as T;
}

export const api = {
  register: (data: { nickName: string; email: string; password: string }) => request<string>('/auth/register', { method: 'POST', body: JSON.stringify(data) }),
  verifyEmail: (data: { email: string; code: string }) => request<string>('/auth/verify-email', { method: 'POST', body: JSON.stringify(data) }),
  login: (data: { email: string; password: string }) => request<{ accessToken: string; refreshToken: string }>('/auth/login', { method: 'POST', body: JSON.stringify(data) }),
  refreshToken: (refreshToken: string) => request<{ accessToken: string; refreshToken: string }>('/auth/refresh-token', { method: 'POST', body: JSON.stringify({ refreshToken }) }),
  logout: (email: string) => request<string>('/auth/logout', { method: 'POST', body: JSON.stringify(email) }),
  requestReset: (email: string) => request<string>('/auth/password-reset/request', { method: 'POST', body: JSON.stringify({ email }) }),
  confirmReset: (data: { token: string; newPassword: string }) => request<string>('/auth/password-reset/confirm', { method: 'POST', body: JSON.stringify(data) }),
  me: (token: string) => request<User>('/api/users/me', { token }),
  categories: (token: string) => request<Category[]>('/api/v1/category/active', { token }),
  products: (token: string) => request<Product[]>('/api/v1/product/activeProducts', { token }),
  productDetail: (token: string, name: string) => request<Product>(`/api/v1/product/detail/${encodeURIComponent(name)}`, { token }),
  search: (token: string, params: string) => request<{ content: Product[]; totalElements: number }>(`/api/v1/product/search?${params}`, { method: 'POST', token }),
  cart: (token: string, userId: string) => request<Cart>(`/api/cart/${userId}`, { token }),
  addCart: (token: string, userId: string, productId: string, quantity: number) => request<Cart>(`/api/cart/${userId}/items`, { method: 'POST', token, body: JSON.stringify({ productId, quantity }) }),
  updateCart: (token: string, userId: string, productId: string, quantity: number) => request<Cart>(`/api/cart/${userId}/items/${productId}`, { method: 'PUT', token, body: JSON.stringify({ quantity }) }),
  removeCart: (token: string, userId: string, productId: string) => request<Cart>(`/api/cart/${userId}/items/${productId}`, { method: 'DELETE', token }),
  clearCart: (token: string, userId: string) => request<void>(`/api/cart/${userId}`, { method: 'DELETE', token }),
  wishlist: (token: string, userId: string) => request<WishlistItem[]>(`/api/wishlist/${userId}`, { token }),
  addWishlist: (token: string, userId: string, productId: string) => request<WishlistItem>(`/api/wishlist/${userId}`, { method: 'POST', token, body: JSON.stringify({ productId }) }),
  removeWishlist: (token: string, userId: string, productId: string) => request<void>(`/api/wishlist/${userId}/products/${productId}`, { method: 'DELETE', token }),
  checkout: (token: string, userId: string) => request<Order>(`/api/orders/checkout/${userId}`, { method: 'POST', token }),
  orders: (token: string, userId: string) => request<Order[]>(`/api/orders/customer/${userId}`, { token }),
  order: (token: string, orderId: string) => request<Order>(`/api/orders/${orderId}`, { token }),
  initializePayment: (token: string, orderId: string) => request<Payment>(`/api/payments/initialize/${orderId}`, { method: 'POST', token }),
  createProduct: (token: string, data: unknown) => request<Product>('/api/v1/product/create', { method: 'POST', token, body: JSON.stringify(data) }),
  deactivateProduct: (token: string, name: string) => request<Product>(`/api/v1/product/deactivate/${encodeURIComponent(name)}`, { method: 'PATCH', token }),
  createCategory: (token: string, data: unknown) => request<Category>('/api/v1/category/create', { method: 'POST', token, body: JSON.stringify(data) }),
  updateCategory: (token: string, id: string, data: unknown) => request<Category>(`/api/v1/category/${id}`, { method: 'PUT', token, body: JSON.stringify(data) }),
  deactivateCategory: (token: string, id: string) => request<Category>(`/api/v1/category/${id}/deactivate`, { method: 'PATCH', token }),
  updateOrderStatus: (token: string, id: string, status: string) => request<Order>(`/api/orders/${id}/status`, { method: 'PATCH', token, body: JSON.stringify({ status }) }),
};