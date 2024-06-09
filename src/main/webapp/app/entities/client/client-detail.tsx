import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './client.reducer';

export const ClientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clientEntity = useAppSelector(state => state.client.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientDetailsHeading">
          <Translate contentKey="pavcoApp.client.detail.title">Client</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clientEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="pavcoApp.client.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{clientEntity.uuid}</dd>
          <dt>
            <span id="ruc">
              <Translate contentKey="pavcoApp.client.ruc">Ruc</Translate>
            </span>
          </dt>
          <dd>{clientEntity.ruc}</dd>
          <dt>
            <span id="businessName">
              <Translate contentKey="pavcoApp.client.businessName">Business Name</Translate>
            </span>
          </dt>
          <dd>{clientEntity.businessName}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="pavcoApp.client.description">Description</Translate>
            </span>
          </dt>
          <dd>{clientEntity.description}</dd>
          <dt>
            <span id="taxPayerType">
              <Translate contentKey="pavcoApp.client.taxPayerType">Tax Payer Type</Translate>
            </span>
          </dt>
          <dd>{clientEntity.taxPayerType}</dd>
          <dt>
            <span id="logo">
              <Translate contentKey="pavcoApp.client.logo">Logo</Translate>
            </span>
          </dt>
          <dd>
            {clientEntity.logo ? (
              <div>
                {clientEntity.logoContentType ? (
                  <a onClick={openFile(clientEntity.logoContentType, clientEntity.logo)}>
                    <img src={`data:${clientEntity.logoContentType};base64,${clientEntity.logo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {clientEntity.logoContentType}, {byteSize(clientEntity.logo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="pavcoApp.client.user">User</Translate>
          </dt>
          <dd>{clientEntity.user ? clientEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/client" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client/${clientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClientDetail;
