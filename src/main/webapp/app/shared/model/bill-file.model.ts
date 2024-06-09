import { IClient } from 'app/shared/model/client.model';

export interface IBillFile {
  id?: number;
  uuid?: string | null;
  name?: string;
  size?: number;
  mimeType?: string;
  contentContentType?: string;
  content?: string;
  isProcessed?: boolean | null;
  client?: IClient | null;
}

export const defaultValue: Readonly<IBillFile> = {
  isProcessed: false,
};
