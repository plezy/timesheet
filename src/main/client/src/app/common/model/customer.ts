import { Address } from './address';

export class CustomerDto {
  id: number;
  name: string;
}

export class Customer extends CustomerDto {
    address: Address;
    useBillingAddress = false;
    billingAddress: Address;
    archived = false;
    deleted = false;
}
