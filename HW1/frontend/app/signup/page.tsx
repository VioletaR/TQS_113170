import { SignupForm } from "../components/SignupForm";

export default function SignupPage() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-4 bg-gray-50">
      <div className="w-full max-w-md space-y-8">
        <div className="text-center">
          <h1 className="text-4xl font-bold tracking-tight">Create Account</h1>
          <p className="mt-2 text-sm">
            Join us to start managing your restaurants and meals
          </p>
        </div>
        <SignupForm />
      </div>
    </main>
  );
} 