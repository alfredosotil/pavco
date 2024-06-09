import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BillFile from './bill-file';
import BillFileDetail from './bill-file-detail';
import BillFileUpdate from './bill-file-update';
import BillFileDeleteDialog from './bill-file-delete-dialog';

const BillFileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BillFile />} />
    <Route path="new" element={<BillFileUpdate />} />
    <Route path=":id">
      <Route index element={<BillFileDetail />} />
      <Route path="edit" element={<BillFileUpdate />} />
      <Route path="delete" element={<BillFileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BillFileRoutes;
