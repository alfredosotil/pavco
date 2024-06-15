import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { IBillFile } from 'app/shared/model/bill-file.model';
import { getEntity, updateEntity, createEntity, reset } from './bill-file.reducer';

export const BillFileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clients = useAppSelector(state => state.client.entities);
  const billFileEntity = useAppSelector(state => state.billFile.entity);
  const loading = useAppSelector(state => state.billFile.loading);
  const updating = useAppSelector(state => state.billFile.updating);
  const updateSuccess = useAppSelector(state => state.billFile.updateSuccess);

  const handleClose = () => {
    navigate('/bill-file');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.size !== undefined && typeof values.size !== 'number') {
      values.size = Number(values.size);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...billFileEntity,
      ...values,
      client: clients.find(it => it.id.toString() === values.client?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...billFileEntity,
          createdDate: convertDateTimeFromServer(billFileEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(billFileEntity.lastModifiedDate),
          client: billFileEntity?.client?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pavcoApp.billFile.home.createOrEditLabel" data-cy="BillFileCreateUpdateHeading">
            <Translate contentKey="pavcoApp.billFile.home.createOrEditLabel">Create or edit a BillFile</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="bill-file-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('pavcoApp.billFile.uuid')}
                id="bill-file-uuid"
                name="uuid"
                data-cy="uuid"
                type="text"
                validate={{}}
              />
              <ValidatedField
                label={translate('pavcoApp.billFile.name')}
                id="bill-file-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 128, message: translate('entity.validation.maxlength', { max: 128 }) },
                }}
              />
              <ValidatedField
                label={translate('pavcoApp.billFile.size')}
                id="bill-file-size"
                name="size"
                data-cy="size"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('pavcoApp.billFile.mimeType')}
                id="bill-file-mimeType"
                name="mimeType"
                data-cy="mimeType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /^[\w]+\/[\w]+$/, message: translate('entity.validation.pattern', { pattern: '^[\\w]+\\/[\\w]+$' }) },
                }}
              />
              <ValidatedBlobField
                label={translate('pavcoApp.billFile.content')}
                id="bill-file-content"
                name="content"
                data-cy="content"
                openActionLabel={translate('entity.action.open')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('pavcoApp.billFile.isProcessed')}
                id="bill-file-isProcessed"
                name="isProcessed"
                data-cy="isProcessed"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('pavcoApp.billFile.createdBy')}
                id="bill-file-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('pavcoApp.billFile.createdDate')}
                id="bill-file-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('pavcoApp.billFile.lastModifiedBy')}
                id="bill-file-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('pavcoApp.billFile.lastModifiedDate')}
                id="bill-file-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="bill-file-client"
                name="client"
                data-cy="client"
                label={translate('pavcoApp.billFile.client')}
                type="select"
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.ruc}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bill-file" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BillFileUpdate;
