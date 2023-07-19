export class DateUtils {
  static formatDate(date: Date): string {
    return `${date.getDate().toString().padStart(2, "0")}/${(
      date.getMonth() + 1
    )
      .toString()
      .padStart(2, "0")}/${date.getFullYear()}, ${date
      .getHours()
      .toString()
      .padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`;
  }

  static getNextHour(date: Date): Date {
    date.setHours(date.getHours() + Math.round(date.getMinutes() / 60));
    date.setMinutes(0, 0, 0); // Resets also seconds and milliseconds
    return date;
  }

  static getNextWeek(date: Date): Date {
    return new Date(date.getTime() + 7 * 24 * 60 * 60 * 1000);
  }

  static getTimeOptions = (selectedDate: Date) => {
    const currentTime = new Date();

    // Min hour is 12pm
    if (currentTime.getHours() < 12) {
      currentTime.setHours(12, 0, 0);
    }

    const currentHour = currentTime.getHours();
    const currentMinutes = currentTime.getMinutes();
    const options = [];

    // Check if the selected date is today
    const isToday = DateUtils.isSameDay(selectedDate, currentTime);

    // Generate time options based on the selected date
    if (isToday) {
      // Generate time options from current time onwards
      for (let hour = currentHour; hour <= 23; hour++) {
        const minutes =
          hour === currentHour ? Math.ceil(currentMinutes / 30) * 30 : 0;

        for (let minute = minutes; minute < 60; minute += 30) {
          const hourString = hour < 10 ? `0${hour}` : `${hour}`;
          const minuteString = minute < 10 ? `0${minute}` : `${minute}`;
          options.push(`${hourString}:${minuteString}`);
        }
      }
    } else {
      // Generate time options for the whole day in half-hour increments
      for (let hour = 12; hour <= 23; hour++) {
        for (let minute = 0; minute < 60; minute += 30) {
          const hourString = hour < 10 ? `0${hour}` : `${hour}`;
          const minuteString = minute < 10 ? `0${minute}` : `${minute}`;
          options.push(`${hourString}:${minuteString}`);
        }
      }
    }

    return options;
  };

  static isSameDay(date1: Date, date2: Date): boolean {
    return (
      date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getDate() === date2.getDate()
    );
  }

  static addTimeToDate(date: Date, time: string): Date {
    const [hours, minutes] = time.split(":");
    date.setHours(Number(hours));
    date.setMinutes(Number(minutes));
    return date;
  }
}
