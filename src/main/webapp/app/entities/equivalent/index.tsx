import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Equivalent from './equivalent';
import EquivalentDetail from './equivalent-detail';
import EquivalentUpdate from './equivalent-update';
import EquivalentDeleteDialog from './equivalent-delete-dialog';

const EquivalentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Equivalent />} />
    <Route path="new" element={<EquivalentUpdate />} />
    <Route path=":id">
      <Route index element={<EquivalentDetail />} />
      <Route path="edit" element={<EquivalentUpdate />} />
      <Route path="delete" element={<EquivalentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EquivalentRoutes;
