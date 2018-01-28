package shared;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import server.StringProcessor;

/**
 * Created by tjense25 on 1/27/18.
 */

public class Command {
    private static Gson gson = new Gson();
    
    private String methodName;
    private String[] parameterTypesNames;
    private Object[] parameters;
    private String[] parametersAsJsonStrings;
    private Class<?>[] parameterTypes;
    
    public Command(String methodName, String[] parameterTypesNames, Object[] parameters) {
        this.methodName = methodName;
        this.parameterTypesNames = parameterTypesNames;
        this.parametersAsJsonStrings = new String[parameterTypesNames.length];
        for (int i = 0; i < parameters.length; i++) {
            parametersAsJsonStrings[i] = gson.toJson(parameters[i]);
        }
    }
    
    public Command(InputStreamReader inputStreamReader) {
        Command tempCommand = (Command) gson.fromJson(inputStreamReader, Command.class);
        
        this.methodName = tempCommand.getMethodName();
        this.parameterTypesNames = tempCommand.getParameterTypesNames();
        this.parametersAsJsonStrings = tempCommand.getParametersAsJsonStrings();
        createParameterTypes();
        this.parameters = new Object[parametersAsJsonStrings.length];
        for(int i = 0; i < parametersAsJsonStrings.length; i++) {
            parameters[i] = gson.fromJson(parametersAsJsonStrings[i], parameterTypes[i]);
        }
        
    }

    public Object execute() {
        Object result = null;

        try {
            Method method = StringProcessor.class.getMethod(methodName, parameterTypes);
            result = method.invoke(StringProcessor.getInstance(), parameters);
        } catch (NoSuchMethodException e) {
            System.err.println("ERROR: Could not find the method " + methodName);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("ERROR: Illegal access while trying to execute the method " + methodName);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Illegal argument while trying to find the method " + methodName);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("ERROR: Illegal Invocation on the target class when calling " + methodName);
            e.printStackTrace();
        } catch (SecurityException e) {
            System.err.println("ERROR: Security error when trying to access the method " + methodName);
        }

        return result;
    }

    private final void createParameterTypes() {
        parameterTypes = new Class<?>[parameterTypesNames.length];
        for(int i = 0; i < parameterTypesNames.length; i++) {
            try {
                parameterTypes[i] = getClassFor(parameterTypesNames[i]);
            } catch (ClassNotFoundException e) {
                System.err.println("ERROR: In Command.execute could not create a aparmeter type from " +
                        "the parameter type name " + parameterTypesNames[i]);
                e.printStackTrace();
            }
        }
    }

    private Class<?> getClassFor(String className) throws ClassNotFoundException {
        Class<?> result = null;
        switch(className) {
            case "boolean" :
                result = boolean.class; break;
            case "byte"    :
                result = byte.class;    break;
            case "char"    :
                result = char.class;    break;
            case "double"  :
                result = double.class;  break;
            case "float"   :
                result = float.class;   break;
            case "int"     :
                result = int.class;     break;
            case "long"    :
                result = long.class;    break;
            case "short"   :
                result = short.class;   break;
            default:
                result = Class.forName(className);
        }
        return result;
    }

    public static Gson getGson() {
        return gson;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParameterTypesNames() {
        return parameterTypesNames;
    }

    public String[] getParametersAsJsonStrings() {
        return parametersAsJsonStrings;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
