import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bill-file.reducer';

export const BillFileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const billFileEntity = useAppSelector(state => state.billFile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billFileDetailsHeading">
          <Translate contentKey="pavcoApp.billFile.detail.title">BillFile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{billFileEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="pavcoApp.billFile.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{billFileEntity.uuid}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="pavcoApp.billFile.name">Name</Translate>
            </span>
          </dt>
          <dd>{billFileEntity.name}</dd>
          <dt>
            <span id="size">
              <Translate contentKey="pavcoApp.billFile.size">Size</Translate>
            </span>
          </dt>
          <dd>{billFileEntity.size}</dd>
          <dt>
            <span id="mimeType">
              <Translate contentKey="pavcoApp.billFile.mimeType">Mime Type</Translate>
            </span>
          </dt>
          <dd>{billFileEntity.mimeType}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="pavcoApp.billFile.content">Content</Translate>
            </span>
          </dt>
          <dd>
            {billFileEntity.content ? (
              <div>
                {billFileEntity.contentContentType ? (
                  <a onClick={openFile(billFileEntity.contentContentType, billFileEntity.content)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {billFileEntity.contentContentType}, {byteSize(billFileEntity.content)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="isProcessed">
              <Translate contentKey="pavcoApp.billFile.isProcessed">Is Processed</Translate>
            </span>
          </dt>
          <dd>{billFileEntity.isProcessed ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="pavcoApp.billFile.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{billFileEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="pavcoApp.billFile.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {billFileEntity.createdDate ? <TextFormat value={billFileEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="pavcoApp.billFile.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{billFileEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="pavcoApp.billFile.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {billFileEntity.lastModifiedDate ? (
              <TextFormat value={billFileEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="pavcoApp.billFile.client">Client</Translate>
          </dt>
          <dd>{billFileEntity.client ? billFileEntity.client.ruc : ''}</dd>
        </dl>
        <Button tag={Link} to="/bill-file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bill-file/${billFileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillFileDetail;
