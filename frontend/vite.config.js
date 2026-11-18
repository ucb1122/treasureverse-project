import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), "");

  return {
    plugins: [react()],
    server: {
      host: "0.0.0.0",
      watch: {
        usePolling: true,
      },
      proxy: {
        "/api": {
          target: env.VITE_BACKEND_PROXY_TARGET ?? "http://localhost:8080",
          changeOrigin: true,
        },
        "/oauth2": {
          target: env.VITE_BACKEND_PROXY_TARGET ?? "http://localhost:8080",
          changeOrigin: true,
        },
        "/login": {
          target: env.VITE_BACKEND_PROXY_TARGET ?? "http://localhost:8080",
          changeOrigin: true,
        },
      },
    },
  };
});
