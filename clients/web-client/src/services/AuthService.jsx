import {apiUrl} from "../BackendApiUrlConfig";

const AuthService = {
    async login(identifier, password) {
        try {
            const response = await fetch(`${apiUrl}/api/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({identifier, password}),
            });

            if (response.status === 409) {
                return false;
            }

            if (response.ok) {
                const accessToken = await response.text();
                localStorage.setItem("accessToken", accessToken);

                return true;
            }
        } catch (error) {
            throw new Error("Ошибка аутентификации");
        }
    },

    async register(username, email, password) {
        try {
            const response = await fetch(`${apiUrl}/api/auth/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({username, email, password}),
            });
            if (!response.ok) {
                return false;
            }
            if (response.ok) {
                const accessToken = await response.text();
                localStorage.setItem("accessToken", accessToken);

                return true;
            }
        } catch (error) {
            throw new Error("Ошибка регистрации");
        }
    },

    async logout() {
        try {
            const response = await fetch(`${apiUrl}/api/auth/logout`, {
                method: "POST",
                credentials: "include",
            });
            if (response.ok || response.status === 204) {
                localStorage.removeItem("accessToken");
                return true;
            }
            return false;
        } catch (error) {
            throw new Error("Ошибка выхода из системы");
        }
    },

    async refreshToken() {
        try {
            const response = await fetch(`${apiUrl}/api/auth/refresh-token`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
            });

            if (response.ok) {
                const accessToken = await response.text();
                localStorage.setItem("accessToken", accessToken);
                return true;
            }
            return false;
        } catch (error) {
            throw new Error("Ошибка обновления refresh токена");
        }
    },

    getAuthToken() {
        return localStorage.getItem("accessToken");
    },

    async fetchJwtClaims() {
        const accessToken = localStorage.getItem("accessToken");

        try {
            const response = await fetch(`${apiUrl}/api/auth/claims`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({accessToken}),
            });

            if (!response.ok) {
                throw new Error("Ошибка при получении claims: " + response.status);
            }

            const data = await response.json();

            if (!data.userId || !Array.isArray(data.roles) || !data.expiresAt) {
                throw new Error("Некорректный формат claims");
            }

            return data;
        } catch (error) {
            console.error("Ошибка доступа:", error);
            return null;
        }
    },

    isAuthenticated() {
        return localStorage.getItem("accessToken") !== null;
    },

    async checkAccessTokenExpired(navigate, expiresAt) {
        try {
            const token = localStorage.getItem("accessToken");
            if (token === null) {
                document.title = "Audio Service";
                navigate("/auth/sign-in");
                localStorage.removeItem("accessToken");
                return false;
            }

            if (expiresAt && new Date(expiresAt) < new Date()) {
                const isRefreshed = this.refreshToken()

                if (isRefreshed) {
                    return true;
                }

                document.title = "Audio Service";
                navigate("/auth/sign-in");
                localStorage.removeItem("accessToken");
                return false;
            }
        } catch (error) {
            console.error("Ошибка валидации токена:", error);
            localStorage.removeItem("accessToken");
            document.title = "Audio Service";
            navigate("/auth/sign-in");
            return false;
        }
    },

    async isValideToken2() {
        try {
            const token = localStorage.getItem("token");
            if (token === null) {
                localStorage.removeItem("token");
                document.title = "Audio Service";
                return false;
            }

            const response = await fetch(`${apiUrl}/api/auth/validate-token`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({token}),
            });

            if (!response.ok) {
                localStorage.removeItem("token");
                // console.log("error");
                return false;
            }

            return true;
        } catch (error) {
            localStorage.removeItem("token");
            document.title = "Audio Service";
            console.error("Ошибка валидации токена:", error);
            return false;
        }
    },
};

export default AuthService;
