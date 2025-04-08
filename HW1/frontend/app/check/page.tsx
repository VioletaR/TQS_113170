import { ReservationCheck } from '@/app/components/ReservationCheck';
import { userMealService } from '@/lib/services/user-meal.service';
import { useState } from 'react';

export default function CheckPage() {
  const [reservation, setReservation] = useState<UserMealDTO | undefined>();

  async function checkReservation(code: string) {
    try {
      const result = await userMealService.getById(parseInt(code));
      setReservation(result);
    } catch (error) {
      console.error('Error checking reservation:', error);
      setReservation(undefined);
    }
  }

  async function verifyReservation() {
    if (reservation) {
      try {
        await userMealService.update({
          ...reservation.userMeal,
          isCheck: true,
        });
        setReservation(undefined);
        alert('Reservation marked as used successfully!');
      } catch (error) {
        console.error('Error verifying reservation:', error);
      }
    }
  }

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Check Reservation</h1>
      <div className="max-w-md mx-auto">
        <ReservationCheck
          reservation={reservation}
          onCheck={checkReservation}
          onVerify={verifyReservation}
        />
      </div>
    </div>
  );
} 