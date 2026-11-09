import { authStorage } from "./auth-storage";

export interface ApiResponse<T = any> {
  success: boolean;
  message?: string;
  data: T;
  timestamp?: string;
  correlationId?: string;
}

export interface ApiRequestOptions extends RequestInit {
  tenantId?: string;
  skipAuth?: boolean;
}

const API_BASE_URL =
  (process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080").replace(/\/$/, "");

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private async request<T>(
    endpoint: string,
    options: ApiRequestOptions = {}
  ): Promise<ApiResponse<T>> {
    const cleanEndpoint = endpoint.startsWith("/") ? endpoint : `/${endpoint}`;
    const url = `${this.baseUrl}${cleanEndpoint}`;

    const headers: Record<string, string> = {
      Accept: "application/json",
      ...(options.headers as Record<string, string>),
    };

    if (!(options.body instanceof FormData) && !headers["Content-Type"]) {
      headers["Content-Type"] = "application/json";
    }

    if (!options.skipAuth) {
      const token = authStorage.getToken();
      if (token) {
        headers["Authorization"] = `Bearer ${token}`;
      }
    }

    const tenantId = options.tenantId || authStorage.getTenantId();
    if (tenantId) {
      headers["X-Tenant-ID"] = tenantId;
    }

    try {
      const response = await fetch(url, {
        ...options,
        headers,
      });

      // Handle 401 Unauthorized Session Expiry
      if (response.status === 401) {
        if (typeof window !== "undefined" && !window.location.pathname.startsWith("/login")) {
          authStorage.clearAuth();
          // Avoid redirect loops if checking credentials
          if (!endpoint.includes("/auth/")) {
            window.location.href = "/login?expired=true";
          }
        }
      }

      const json = await response.json();

      if (!response.ok || (json && json.success === false)) {
        const errorMsg = json?.message || `API request failed with status ${response.status}`;
        throw new Error(errorMsg);
      }

      return json as ApiResponse<T>;
    } catch (error: any) {
      // Re-throw with descriptive context
      throw new Error(error.message || "Network connection failure with School ERP Backend");
    }
  }

  public get<T>(endpoint: string, options?: ApiRequestOptions): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { ...options, method: "GET" });
  }

  public post<T>(
    endpoint: string,
    body?: any,
    options?: ApiRequestOptions
  ): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      ...options,
      method: "POST",
      body: body instanceof FormData ? body : JSON.stringify(body),
    });
  }

  public put<T>(
    endpoint: string,
    body?: any,
    options?: ApiRequestOptions
  ): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      ...options,
      method: "PUT",
      body: body instanceof FormData ? body : JSON.stringify(body),
    });
  }

  public patch<T>(
    endpoint: string,
    body?: any,
    options?: ApiRequestOptions
  ): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      ...options,
      method: "PATCH",
      body: body instanceof FormData ? body : JSON.stringify(body),
    });
  }

  public delete<T>(endpoint: string, options?: ApiRequestOptions): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { ...options, method: "DELETE" });
  }
}

export const apiClient = new ApiClient(API_BASE_URL);

