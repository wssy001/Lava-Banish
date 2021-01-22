package cyou.wssy001.banish.service;

import moe.ofs.backend.domain.connector.handlers.scripts.LuaScriptStarter;
import moe.ofs.backend.domain.connector.handlers.scripts.ScriptInjectionTask;
import moe.ofs.backend.function.mizdb.services.impl.LuaStorageInitServiceImpl;
import moe.ofs.backend.hookinterceptor.HookInterceptorDefinition;
import moe.ofs.backend.hookinterceptor.HookType;
import org.springframework.stereotype.Service;

@Service
public class PlayerBanishServiceImpl implements LuaScriptStarter {
    @Override
    public ScriptInjectionTask injectScript() {
        return ScriptInjectionTask.builder()
                .scriptIdentName("PlayerConnectionValidationService")
                .initializrClass(getClass())
                .dependencyInitializrClass(LuaStorageInitServiceImpl.class)
                .inject(() -> {
                    boolean hooked = createHook(getClass().getName(), HookType.ON_PLAYER_TRY_CONNECT);

                    HookInterceptorDefinition hookInterceptorDefinition =
                            HookInterceptorDefinition.builder()
                                    .name("lava-default-player-connection-validation-hook-interceptor")
                                    .storage(connectionValidatorStorage)
//                                    .predicateFunction("" +
//                                            "function(" + HookType.ON_PLAYER_TRY_CONNECT.getFunctionArgsString("store") + ") " +
//                                            "   net.log('enter sandman') " +
//                                            "end")
                                    .argPostProcessFunction("" +
                                            "function(" + HookType.ON_PLAYER_TRY_CONNECT.getFunctionArgsString("store") + ") " +
                                            "   local data = { " +
                                            "       ipaddr = addr, " +
                                            "       playerName = name, " +
                                            "       ucid = ucid " +
                                            "   } " +
                                            "   return data " +
                                            "end")
                                    .build();

                    HookInterceptorDefinition globalBlockDefinition = HookInterceptorDefinition.builder()
                            .name("lava-default-player-connection-block-hook-interceptor")
                            .storage(globalConnectionBlockStorage)
                            .predicateFunction("" +
                                    "function(store, ...) " +
                                    "   net.log('global block function test enter') " +
                                    "   if store and store:get('isBlocked') then " +
                                    "       return false, store:get('blockReason') " +
                                    "   end " +
                                    "end ")
                            .argPostProcessFunction("" +
                                    "function(" + HookType.ON_PLAYER_TRY_CONNECT.getFunctionArgsString("store") + ") " +
                                    "   local data = { " +
                                    "       ipaddr = addr, " +
                                    "       playerName = name, " +
                                    "       ucid = ucid " +
                                    "   } " +
                                    "   return data " +
                                    "end")
                            .build();

                    return hooked &&
                            addDefinition(hookInterceptorDefinition) &&
                            addDefinition(globalBlockDefinition);
                })
                .injectionDoneCallback(aBoolean -> {
                    if (aBoolean) log.info("Hook Interceptor Initialized: {}", getName());
                    else log.error("Failed to initiate hook interceptor: {}", getName());
                })
                .build();
    }
}
