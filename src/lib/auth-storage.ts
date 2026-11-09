export interface StoredUser {
  id: string;
  name: string;
  email: string;
  roles: string[];
}

export interface StoredTenant {
  id: string;
  name: string;
  code: string;
}

const TOKEN_KEY = "schoolerp_token";
const USER_KEY = "schoolerp_user";
const TENANT_KEY = "schoolerp_tenant";
const DEFAULT_TENANT_ID = "tenant_apex_mumbai_01";

export const authStorage = {
  getToken(): string | null {
    if (typeof window === "undefined") return null;
    try {
      return localStorage.getItem(TOKEN_KEY);
    } catch {
      return null;
    }
  },

  setToken(token: string): void {
    if (typeof window === "undefined") return;
    try {
      localStorage.setItem(TOKEN_KEY, token);
    } catch (e) {
      console.error("Failed to persist token", e);
    }
  },

  getUser(): StoredUser | null {
    if (typeof window === "undefined") return null;
    try {
      const data = localStorage.getItem(USER_KEY);
      return data ? JSON.parse(data) : null;
    } catch {
      return null;
    }
  },

  setUser(user: StoredUser): void {
    if (typeof window === "undefined") return;
    try {
      localStorage.setItem(USER_KEY, JSON.stringify(user));
    } catch (e) {
      console.error("Failed to persist user", e);
    }
  },

  getTenant(): StoredTenant | null {
    if (typeof window === "undefined") return null;
    try {
      const data = localStorage.getItem(TENANT_KEY);
      return data ? JSON.parse(data) : null;
    } catch {
      return null;
    }
  },

  getTenantId(): string {
    const tenant = this.getTenant();
    return tenant?.id || DEFAULT_TENANT_ID;
  },

  setTenant(tenant: StoredTenant): void {
    if (typeof window === "undefined") return;
    try {
      localStorage.setItem(TENANT_KEY, JSON.stringify(tenant));
    } catch (e) {
      console.error("Failed to persist tenant", e);
    }
  },

  clearAuth(): void {
    if (typeof window === "undefined") return;
    try {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USER_KEY);
      localStorage.removeItem(TENANT_KEY);
    } catch (e) {
      console.error("Failed to clear auth storage", e);
    }
  },

  isAuthenticated(): boolean {
    return !!this.getToken();
  },
};

