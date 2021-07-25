package org.h2.api;
import java.security.NoSuchAlgorithmException;
import java.sql.Types;
import java.util.Locale;
import org.h2.message.DbException;
import org.h2.store.DataHandler;
import org.h2.util.JdbcUtils;
import org.h2.value.DataType;
import org.h2.value.Password;
import org.h2.value.Value;
import org.h2.value.ValuePassword;
import org.h2.value.ValueJavaObject;
import org.h2.value.ExtTypeInfo;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Locale;
import org.h2.api.CustomDataTypesHandler;
import org.h2.api.ErrorCode;
import org.h2.engine.CastDataProvider;
import org.h2.message.DbException;
import org.h2.store.DataHandler;
import org.h2.util.JdbcUtils;
import org.h2.util.StringUtils;
import org.h2.value.CompareMode;
import org.h2.value.DataType;
import org.h2.value.ExtTypeInfo;
import org.h2.value.TypeInfo;
import org.h2.value.Value;
import org.h2.value.ValueBytes;
import org.h2.value.ValueDouble;
import org.h2.value.ValueJavaObject;
import org.h2.value.ValueString;



public class CustomPassword implements CustomDataTypesHandler{

    public final static String password = "PASSWORD";
    public final DataType passwordDataType = new DataType();;

    public CustomPassword() {
        passwordDataType.name = password;
        passwordDataType.type = Value.PASSWORD;
        passwordDataType.sqlType = Types.JAVA_OBJECT;
        //passwordDataType = incomingDatatype;
    }

    @Override
    public DataType getDataTypeByName(String name) {
        if (name.toUpperCase(Locale.ENGLISH).equals(password)){
            return passwordDataType;
        }else{
            return null;
        }
    }

    @Override
    public DataType getDataTypeById(int type) {
        if (type == Value.PASSWORD){
            return passwordDataType;
        }else{
            return null;
        }
    }

    @Override
    public TypeInfo getTypeInfoById(int type, long precision, int scale, ExtTypeInfo extTypeInfo) {
        if (type == Value.PASSWORD){
            return new TypeInfo(Value.PASSWORD, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, null);
        }else{
            return null;
        }
    }


    @Override
    public int getDataTypeOrder(int type) {
        if (type == Value.PASSWORD){
            return 53_000;
        }else{
            throw DbException.get(ErrorCode.UNKNOWN_DATA_TYPE_1, "type:" + type);
        }
    }



    @Override
    public Value convert(Value source, int targetType) {

        if (source.getValueType() == targetType) {
            return source;
        }
        if (targetType == Value.PASSWORD) {
            switch (source.getValueType()) {
                case Value.JAVA_OBJECT: {
                    try {
                        assert source instanceof ValueJavaObject;
                        return ValuePassword.get((Password)
                                JdbcUtils.deserialize(source.getBytesNoCopy(), null));
                    }
                    catch (NoSuchAlgorithmException nsae) {
                        nsae.printStackTrace();
                    }
                }
                case Value.STRING: {
                    try {
                        assert source instanceof ValueString;
                        return ValuePassword.get(new Password(source.getString()));
                    }
                    catch (NoSuchAlgorithmException nsae) {
                        nsae.printStackTrace();
                    }
                }
                case Value.BYTES: {
                    try {
                        assert source instanceof ValueBytes;
                        return ValuePassword.get((Password)
                                JdbcUtils.deserialize(source.getBytesNoCopy(), null));
                    }
                    catch (NoSuchAlgorithmException nsae) {
                        nsae.printStackTrace();
                    }
                }
                case Value.DOUBLE: {
                    try {
                        assert source instanceof ValueDouble;
                        return ValuePassword.get(new Password(source.getString()));
                    }
                    catch (NoSuchAlgorithmException nsae) {
                        nsae.printStackTrace();
                    }
                }
            }

            throw DbException.get(
                    ErrorCode.DATA_CONVERSION_ERROR_1, source.getString());
        } else {
            return source.convertTo(targetType);
        }
    }


    @Override
    public String getDataTypeClassName(int type) {
        if (type == Value.PASSWORD){
            return Password.class.getName();
        }
        else{
            throw DbException.get(ErrorCode.UNKNOWN_DATA_TYPE_1, "type:"+type);
        }
    }


    @Override
    public int getTypeIdFromClass(Class<?> cls) {
        if (cls == Password.class){
            return Value.PASSWORD;
        }
        return Value.JAVA_OBJECT;
    }

    @Override
    public Value getValue(int type, Object data, DataHandler dataHandler) {
        System.out.println("Get the Value: "+type+" "+data.getClass());
        if (type == Value.PASSWORD && data instanceof Password) {
            try {
                return ValuePassword.get((Password) data);
            }
            catch (NoSuchAlgorithmException nsae) {
                nsae.printStackTrace();
            }
        }
        return ValueJavaObject.getNoCopy(data, null, dataHandler);
    }

    @Override
    public Object getObject(Value value, Class<?> cls) {

        if (cls.equals(Password.class)) {
            if (value.getValueType() == Value.PASSWORD) {
                return value.getObject();
            }
            return convert(value, Value.PASSWORD).getObject();
        }
        throw DbException.get(
                ErrorCode.UNKNOWN_DATA_TYPE_1, "type:" + value.getValueType());
    }

    @Override
    public boolean supportsAdd(int type) {
        return false;
    }

    @Override
    public int getAddProofType(int type) {
        if (type == Value.PASSWORD)
            return type;
        else
            throw DbException.get(ErrorCode.UNKNOWN_DATA_TYPE_1, "type:" + type);
    }



}