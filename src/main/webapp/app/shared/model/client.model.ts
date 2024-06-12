import { IUser } from 'app/shared/model/user.model';
import { TaxPayerType } from 'app/shared/model/enumerations/tax-payer-type.model';

export interface IClient {
  id?: number;
  uuid?: string | null;
  email?: string;
  ruc?: string;
  businessName?: string;
  description?: string | null;
  taxPayerType?: keyof typeof TaxPayerType;
  logoContentType?: string | null;
  logo?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IClient> = {};
