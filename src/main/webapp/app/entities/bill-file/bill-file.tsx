import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './bill-file.reducer';

export const BillFile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const billFileList = useAppSelector(state => state.billFile.entities);
  const loading = useAppSelector(state => state.billFile.loading);

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
      <h2 id="bill-file-heading" data-cy="BillFileHeading">
        <Translate contentKey="pavcoApp.billFile.home.title">Bill Files</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="pavcoApp.billFile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/bill-file/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="pavcoApp.billFile.home.createLabel">Create new Bill File</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {billFileList && billFileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="pavcoApp.billFile.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('uuid')}>
                  <Translate contentKey="pavcoApp.billFile.uuid">Uuid</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('uuid')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="pavcoApp.billFile.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('size')}>
                  <Translate contentKey="pavcoApp.billFile.size">Size</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('size')} />
                </th>
                <th className="hand" onClick={sort('mimeType')}>
                  <Translate contentKey="pavcoApp.billFile.mimeType">Mime Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mimeType')} />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="pavcoApp.billFile.content">Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('isProcessed')}>
                  <Translate contentKey="pavcoApp.billFile.isProcessed">Is Processed</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isProcessed')} />
                </th>
                <th>
                  <Translate contentKey="pavcoApp.billFile.client">Client</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {billFileList.map((billFile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bill-file/${billFile.id}`} color="link" size="sm">
                      {billFile.id}
                    </Button>
                  </td>
                  <td>{billFile.uuid}</td>
                  <td>{billFile.name}</td>
                  <td>{billFile.size}</td>
                  <td>{billFile.mimeType}</td>
                  <td>
                    {billFile.content ? (
                      <div>
                        {billFile.contentContentType ? (
                          <a onClick={openFile(billFile.contentContentType, billFile.content)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {billFile.contentContentType}, {byteSize(billFile.content)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{billFile.isProcessed ? 'true' : 'false'}</td>
                  <td>{billFile.client ? <Link to={`/client/${billFile.client.id}`}>{billFile.client.ruc}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bill-file/${billFile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/bill-file/${billFile.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/bill-file/${billFile.id}/delete`)}
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
              <Translate contentKey="pavcoApp.billFile.home.notFound">No Bill Files found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default BillFile;
