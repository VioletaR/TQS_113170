"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";
import { userMealService } from "@/lib/services/user-meal.service";
import { useRouter } from "next/navigation";
import { useAuth } from "../contexts/AuthContext";

interface BookingFormProps {
  mealId: string;
}

export function BookingForm({ mealId }: BookingFormProps) {
  const router = useRouter();
  const { isAuthenticated } = useAuth();
  const [isLoading, setIsLoading] = useState(false);
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [reservationCode, setReservationCode] = useState("");

  const handleBooking = async () => {
    if (!isAuthenticated) {
      router.push("/login");
      return;
    }

    setIsLoading(true);
    try {
      const response = await userMealService.create({ mealId });
      setReservationCode(response.reservationCode);
      setShowConfirmation(true);
    } catch (error) {
      console.error("Failed to book meal:", error);
      alert("Failed to book meal. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Button 
        onClick={handleBooking} 
        disabled={isLoading}
        className="w-full"
      >
        {isLoading ? "Booking..." : "Book Now"}
      </Button>

      <Dialog open={showConfirmation} onOpenChange={setShowConfirmation}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Booking Confirmed!</DialogTitle>
            <DialogDescription>
              Your meal has been successfully booked. Please save your reservation code:
            </DialogDescription>
          </DialogHeader>
          <div className="p-4 bg-gray-100 rounded-md">
            <code className="text-lg font-mono">{reservationCode}</code>
          </div>
          <DialogFooter className="flex gap-2">
            <Button
              variant="outline"
              onClick={() => {
                setShowConfirmation(false);
                router.push("/my-meals");
              }}
            >
              View My Meals
            </Button>
            <Button
              onClick={() => {
                setShowConfirmation(false);
              }}
            >
              Close
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
} 