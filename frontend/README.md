# Nova Market mobile app

A compact Expo demo client for the eCommerce Spring Boot API.

## Run

```bash
npm install
cp .env.example .env
npm start
```

Set `EXPO_PUBLIC_API_URL` to a host reachable by the device. `localhost` works for a simulator running on the same machine; a physical device needs the computer's LAN IP.

## Backend coverage

- Auth: register, verify email, login, refresh token, logout, password reset request and confirmation
- Identity: current signed-in user via `/api/users/me`
- Catalog: active products, active categories, product detail, filtered search
- Shopping: cart read/add/update/remove/clear and wishlist read/add/remove
- Orders: checkout, customer order history, order detail, status update
- Payments: Paystack initialization and the backend webhook contract
- Admin API client methods: product create/deactivate, category create/update/deactivate, and order status update

The backend currently authenticates all `/api/**` routes but does not enforce `ROLE_ADMIN` at controller level. Keep admin controls behind your own role policy before production use.
