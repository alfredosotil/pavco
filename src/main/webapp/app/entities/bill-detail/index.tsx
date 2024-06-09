import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BillDetail from './bill-detail';
import BillDetailDetail from './bill-detail-detail';
import BillDetailUpdate from './bill-detail-update';
import BillDetailDeleteDialog from './bill-detail-delete-dialog';

const BillDetailRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BillDetail />} />
    <Route path="new" element={<BillDetailUpdate />} />
    <Route path=":id">
      <Route index element={<BillDetailDetail />} />
      <Route path="edit" element={<BillDetailUpdate />} />
      <Route path="delete" element={<BillDetailDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BillDetailRoutes;
