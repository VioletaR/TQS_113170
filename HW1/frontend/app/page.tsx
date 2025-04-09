import { LoginForm } from "./components/LoginForm";

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-4 bg-gray-50">
      <div className="w-full max-w-md space-y-8">
        <div className="text-center">
          <h1 className="text-4xl font-bold tracking-tight">Welcome Back</h1>
          <p className="mt-2 text-sm">
            Please sign in to your account to continue
          </p>
        </div>
        <LoginForm />
      </div>
    </main>
  );
}
