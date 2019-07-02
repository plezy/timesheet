import { Address } from './address';

export class Customer {

    id: number;
    name: string;
    address: Address;
    billingAddress: Address;
    archived: boolean;
    deleted: boolean;

}
