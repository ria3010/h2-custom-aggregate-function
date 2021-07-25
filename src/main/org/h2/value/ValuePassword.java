package org.h2.value;

import org.h2.engine.CastDataProvider;
import org.h2.engine.CastDataProvider;
import org.h2.engine.SysProperties;
import org.h2.util.StringUtils;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.h2.api.ErrorCode;
import org.h2.engine.Mode;
import org.h2.message.DbException;
import org.h2.util.JdbcUtils;

public class ValuePassword extends Value{

    private TypeInfo type;

    Password password;

    ValuePassword(String value) throws NoSuchAlgorithmException{
        this.password = new Password(value);
    }

    ValuePassword(Password pass) {
        this.password =pass;
    }

    public static Value get(Password pass) throws NoSuchAlgorithmException {
        ValuePassword valuePass = new ValuePassword(pass);
        return valuePass;
    }

    @Override
    public Value convertTo(int targetType, ExtTypeInfo extTypeInfo, CastDataProvider provider,
                              boolean forComparison, Object column) {

        if (getValueType() == targetType) {
            return this;
        }
        switch (targetType) {
            case Value.BYTES: {
                return ValueBytes.getNoCopy(JdbcUtils.serialize(password, null));
            }
            case Value.STRING: {
                return ValueString.get(password.toString());
            }
            case Value.JAVA_OBJECT: {
                return ValueJavaObject.getNoCopy(JdbcUtils.serialize(password, null));
            }
        }

        throw DbException.get(
                ErrorCode.DATA_CONVERSION_ERROR_1, getString());
    }



    @Override
    public StringBuilder getSQL(StringBuilder builder) {
        return builder.append(password.toString());
    }


    @Override
    public final TypeInfo getType() {
        TypeInfo type = this.type;
        if (type == null) {
            int length = password.toString().length();
            this.type =type= new TypeInfo(getValueType(), 0, 0, 0, null);
        }
        return type;
    }

    @Override
    public int getValueType() {
        return PASSWORD;
    }


    @Override
    public int compareTypeSafe(Value o, CompareMode mode, CastDataProvider provider) {
        ValuePassword passV = (ValuePassword) o.getObject();
        return mode.compareString(this.password.toString(), passV.password.toString(), false);
    }

    @Override
    public String getString() {
        return password.toString();
    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }

    @Override
    public Object getObject() {
        return new ValuePassword(password);
    }

    @Override
    public void set(PreparedStatement prep, int parameterIndex)
            throws SQLException {
        prep.setString(parameterIndex, password.toString());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ValuePassword && password == ((ValuePassword) other).password;
    }

}

