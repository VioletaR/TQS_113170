"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { useAuth } from "../contexts/AuthContext";

export function Navigation() {
  const pathname = usePathname();
  const router = useRouter();
  const { isAuthenticated, logout, user } = useAuth();

  const isRoot = pathname === "/";
  const isLogin = pathname === "/login";
  const isSignup = pathname === "/signup";

  // Hide navigation on these pages
  if (isRoot || isLogin || isSignup ) {
    return null;
  }

  const handleLogout = () => {
    // Clear auth state and local storage
    logout();
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    router.push("/");
  };

  const handleLogin = () => {
    // Clear any existing auth state and local storage
    logout();
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    // Force a hard navigation to ensure complete state reset
    window.location.href = "/login";
  };

  return (
    <nav className="bg-white shadow-sm" data-testid="navigation">
      <div className="container mx-auto px-4 py-3 flex justify-between items-center">
        <div className="flex items-center space-x-4">
          <Button 
            variant="ghost" 
            onClick={() => router.back()}
            data-testid="back-button"
          >
            Back
          </Button>
          <Link href="/restaurants">
            <Button 
              variant="ghost"
              data-testid="restaurants-button"
            >
              Restaurants
            </Button>
          </Link>
        </div>

        <div className="flex items-center space-x-4">
          {isAuthenticated ? (
            <>
              {user?.role !== "STAFF" && (
                <Link href="/my-meals">
                  <Button 
                    variant="ghost"
                    data-testid="my-meals-button"
                  >
                    My Meals
                  </Button>
                </Link>
              )}
              <Button 
                variant="ghost" 
                onClick={handleLogout}
                data-testid="logout-button"
              >
                Logout
              </Button>
            </>
          ) : (
            <Button 
              variant="ghost" 
              onClick={handleLogin}
              data-testid="login-button"
            >
              Login
            </Button>
          )}
        </div>
      </div>
    </nav>
  );
} 