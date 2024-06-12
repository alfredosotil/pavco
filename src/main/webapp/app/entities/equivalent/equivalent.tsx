import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './equivalent.reducer';

export const Equivalent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const equivalentList = useAppSelector(state => state.equivalent.entities);
  const loading = useAppSelector(state => state.equivalent.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="equivalent-heading" data-cy="EquivalentHeading">
        <Translate contentKey="pavcoApp.equivalent.home.title">Equivalents</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="pavcoApp.equivalent.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/equivalent/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="pavcoApp.equivalent.home.createLabel">Create new Equivalent</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {equivalentList && equivalentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="pavcoApp.equivalent.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('uuid')}>
                  <Translate contentKey="pavcoApp.equivalent.uuid">Uuid</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uuid')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="pavcoApp.equivalent.code">Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="pavcoApp.equivalent.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="pavcoApp.equivalent.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('discount')}>
                  <Translate contentKey="pavcoApp.equivalent.discount">Discount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discount')} />
                </th>
                <th>
                  <Translate contentKey="pavcoApp.equivalent.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="pavcoApp.equivalent.client">Client</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {equivalentList.map((equivalent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/equivalent/${equivalent.id}`} color="link" size="sm">
                      {equivalent.id}
                    </Button>
                  </td>
                  <td>{equivalent.uuid}</td>
                  <td>{equivalent.code}</td>
                  <td>{equivalent.name}</td>
                  <td>{equivalent.price}</td>
                  <td>{equivalent.discount}</td>
                  <td>{equivalent.product ? <Link to={`/product/${equivalent.product.id}`}>{equivalent.product.code}</Link> : ''}</td>
                  <td>{equivalent.client ? <Link to={`/client/${equivalent.client.id}`}>{equivalent.client.ruc}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/equivalent/${equivalent.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/equivalent/${equivalent.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/equivalent/${equivalent.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="pavcoApp.equivalent.home.notFound">No Equivalents found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Equivalent;
