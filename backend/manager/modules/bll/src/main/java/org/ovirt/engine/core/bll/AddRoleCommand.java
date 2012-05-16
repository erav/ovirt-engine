package org.ovirt.engine.core.bll;

import java.util.Collections;
import java.util.List;

import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.PermissionSubject;
import org.ovirt.engine.core.common.VdcObjectType;
import org.ovirt.engine.core.common.action.RolesOperationsParameters;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dal.VdcBllMessages;

public class AddRoleCommand<T extends RolesOperationsParameters> extends RolesOperationCommandBase<T> {
    private static final long serialVersionUID = 2431768094305319479L;

    public AddRoleCommand(T parameters) {
        super(parameters);
    }

    @Override
    protected boolean canDoAction() {
        boolean returnValue = true;
        if (getRoleDao().getByName(getRole().getname()) != null) {
            addCanDoActionMessage(VdcBllMessages.VAR__ACTION__ADD);
            addCanDoActionMessage(VdcBllMessages.ERROR_CANNOT_UPDATE_ROLE_NAME);
            returnValue = false;

        }
        return returnValue;
    }

    @Override
    public AuditLogType getAuditLogTypeValue() {
        return getSucceeded() ? AuditLogType.USER_ADD_ROLE : AuditLogType.USER_ADD_ROLE_FAILED;
    }

    @Override
    protected void executeCommand() {
        getRole().setId(Guid.NewGuid());
        getRoleDao().save(getRole());
        getReturnValue().setActionReturnValue(getRole().getId());
        setSucceeded(true);
    }

    @Override
    public List<PermissionSubject> getPermissionCheckSubjects() {
        return Collections.singletonList(new PermissionSubject(MultiLevelAdministrationHandler.SYSTEM_OBJECT_ID,
                VdcObjectType.System,
                getActionType().getActionGroup()));
    }
}
