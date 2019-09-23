export class Contract {
    id: number;
    name: string;
    description: string;
    archived = false;
    deleted = false;
    contractType: string;
    orderNumber: string;
    orderDate: Date;
    plannedStart: Date;
    plannedEnd: Date;
}

export class ContractDto {
  id: number;
  name: string;
  contractType: string;
  customerId: number;
  customerName: string;
  plannedStart: Date;
  plannedEnd: Date;
  archived = false;
  deleted = false;
}
