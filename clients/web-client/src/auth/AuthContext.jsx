import PropTypes from "prop-types";
import React, {createContext, useContext, useEffect, useState} from "react";
import {apiUrl} from "../BackendApiUrlConfig";
import AuthService from "../services/AuthService";
// import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export const useAuthContext = () => useContext(AuthContext);

export const AuthProvider = ({children}) => {

    const claims = {
        userId: "",
        roles: [],
        expiresAt: "" // Instant -> ISO 8601
    }

    const [jwtClaims, setJwtClaims] = useState(claims);

    const [isAuthenticated, setIsAuthenticated] = useState(
        AuthService.isAuthenticated()
    );
    const [isValidToken, setIsValidToken] = useState(
        AuthService.isAuthenticated()
    );
    const [isTokenExists, setIsTokenExists] = useState(
        AuthService.isAuthenticated()
    );

    const [profileImage, setProfileImage] = useState(null);
    const [isProfileImageUpdated, setIsProfileImageUpdated] = useState(false);
    const [isProfileImageDeleted, setIsProfileImageDeleted] = useState(false);
    const [profileData, setProfileData] = useState(null);

    const [favoriteContainerId, setFavoriteContainerId] = useState(null);

    async function fetchJwtClaims() {
        try {
            const claims = await AuthService.fetchJwtClaims();
            setJwtClaims({
                userId: claims.userId,
                roles: claims.roles,
                expiresAt: claims.expiresAt
            });
        } catch (error) {
            console.error("Ошибка при получении jwt claims", error);
        }
    }

    useEffect(() => {
        fetchJwtClaims();
    }, [isAuthenticated]);

    useEffect(() => {
        setIsAuthenticated(AuthService.isAuthenticated());

        // setTimeout(() => {
        //     setIsProfileImageUpdated(false);
        // }, 1000);
        // // setTimeout(() => {
        // AuthService.isValideToken2().then(result => {
        //     setIsAuthenticated(result);
        //     if (!result) {
        //         AuthService.logout();
        //         setProfileData(null);
        //
        //         document.title = "Audio Service";
        //     } else {
        //         fetchProfileData();
        //     }
        // });
    }, [isAuthenticated, isProfileImageUpdated]);

    const fetchProfileData = () => {
        // setProfileData(null);
        fetch(`${apiUrl}/api/profile`, {
            headers: {
                Authorization: `Bearer ${AuthService.getAuthToken()}`,
            },
            method: "GET",
            // signal: abortController.signal,
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                return null;
            })
            .then(data => {
                if (data) {
                    setProfileData(data);
                }
            });
    };

    return (
        <AuthContext.Provider
            value={{
                isAuthenticated,
                setIsAuthenticated,
                isValidToken,
                setIsValidToken,
                profileImage,
                setProfileImage,
                profileData,
                setProfileData,
                isProfileImageUpdated,
                setIsProfileImageUpdated,
                setIsProfileImageDeleted,
                fetchProfileData,
                favoriteContainerId,
                setFavoriteContainerId,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

AuthProvider.propTypes = {
    children: PropTypes.node.isRequired,
};
