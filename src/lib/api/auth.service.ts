import { apiClient, ApiResponse } from "../api-client";
import { authStorage, StoredUser, StoredTenant } from "../auth-storage";

export interface LoginResponseData {
  token: string;
  tokenType: string;
  user: StoredUser;
  tenant: StoredTenant;
}

export const authService = {
  async checkHealth(): Promise<{ status: string }> {
    try {
      const res = await apiClient.get<{ status: string }>("/api/v1/auth/health", {
        skipAuth: true,
      });
      return res.data;
    } catch {
      return { status: "DOWN" };
    }
  },

  async login(
    email: string,
    password: string,
    tenantId = "tenant_apex_mumbai_01"
  ): Promise<LoginResponseData> {
    const res = await apiClient.post<LoginResponseData>(
      "/api/v1/auth/login",
      { email, password },
      { tenantId, skipAuth: true }
    );

    if (res.data && res.data.token) {
      authStorage.setToken(res.data.token);
      authStorage.setUser(res.data.user);
      authStorage.setTenant(res.data.tenant);
    }

    return res.data;
  },

  logout(): void {
    authStorage.clearAuth();
    if (typeof window !== "undefined") {
      window.location.href = "/login";
    }
  },

  getCurrentUser(): StoredUser | null {
    return authStorage.getUser();
  },

  isAuthenticated(): boolean {
    return authStorage.isAuthenticated();
  },
};

