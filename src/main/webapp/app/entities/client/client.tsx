import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './client.reducer';

export const Client = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const clientList = useAppSelector(state => state.client.entities);
  const loading = useAppSelector(state => state.client.loading);

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
      <h2 id="client-heading" data-cy="ClientHeading">
        <Translate contentKey="pavcoApp.client.home.title">Clients</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="pavcoApp.client.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/client/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="pavcoApp.client.home.createLabel">Create new Client</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {clientList && clientList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="pavcoApp.client.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('uuid')}>
                  <Translate contentKey="pavcoApp.client.uuid">Uuid</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('uuid')} />
                </th>
                <th className="hand" onClick={sort('ruc')}>
                  <Translate contentKey="pavcoApp.client.ruc">Ruc</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('ruc')} />
                </th>
                <th className="hand" onClick={sort('businessName')}>
                  <Translate contentKey="pavcoApp.client.businessName">Business Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('businessName')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="pavcoApp.client.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('taxPayerType')}>
                  <Translate contentKey="pavcoApp.client.taxPayerType">Tax Payer Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('taxPayerType')} />
                </th>
                <th className="hand" onClick={sort('logo')}>
                  <Translate contentKey="pavcoApp.client.logo">Logo</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('logo')} />
                </th>
                <th>
                  <Translate contentKey="pavcoApp.client.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {clientList.map((client, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/client/${client.id}`} color="link" size="sm">
                      {client.id}
                    </Button>
                  </td>
                  <td>{client.uuid}</td>
                  <td>{client.ruc}</td>
                  <td>{client.businessName}</td>
                  <td>{client.description}</td>
                  <td>
                    <Translate contentKey={`pavcoApp.TaxPayerType.${client.taxPayerType}`} />
                  </td>
                  <td>
                    {client.logo ? (
                      <div>
                        {client.logoContentType ? (
                          <a onClick={openFile(client.logoContentType, client.logo)}>
                            <img src={`data:${client.logoContentType};base64,${client.logo}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {client.logoContentType}, {byteSize(client.logo)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{client.user ? client.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/client/${client.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/client/${client.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/client/${client.id}/delete`)}
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
              <Translate contentKey="pavcoApp.client.home.notFound">No Clients found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Client;
