import { Address } from './address';

export class Customer {
    id: number;
    name: string;
    address: Address;
    useBillingAddress = false;
    billingAddress: Address;
    archived = false;
    deleted = false;
}
