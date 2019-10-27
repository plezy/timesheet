export class MessageDto {
  id: Number;
  number: Number = 10;
  message: string;

  constructor(message: string) {
    this.message = message;
  }
}
