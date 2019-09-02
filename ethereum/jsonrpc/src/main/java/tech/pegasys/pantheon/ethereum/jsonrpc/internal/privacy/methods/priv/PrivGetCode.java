/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.pantheon.ethereum.jsonrpc.internal.privacy.methods.priv;

import tech.pegasys.pantheon.ethereum.core.Address;
import tech.pegasys.pantheon.ethereum.jsonrpc.RpcMethod;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.JsonRpcRequest;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.methods.JsonRpcMethod;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.parameters.JsonRpcParameter;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.response.JsonRpcResponse;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.response.JsonRpcSuccessResponse;
import tech.pegasys.pantheon.ethereum.privacy.PrivateTransactionHandler;
import tech.pegasys.pantheon.util.bytes.BytesValue;

public class PrivGetCode implements JsonRpcMethod {

  private final JsonRpcParameter parameters;
  private final PrivateTransactionHandler privateTransactionHandler;

  public PrivGetCode(
      final JsonRpcParameter parameters,
      final PrivateTransactionHandler privateTransactionHandler) {
    this.parameters = parameters;
    this.privateTransactionHandler = privateTransactionHandler;
  }

  @Override
  public String getName() {
    return RpcMethod.PRIV_GET_CODE.getMethodName();
  }

  @Override
  public JsonRpcResponse response(final JsonRpcRequest request) {
    final Address address = parameters.required(request.getParams(), 0, Address.class);
    final String privacyGroupId = parameters.required(request.getParams(), 2, String.class);

    final BytesValue byteCode = privateTransactionHandler.getAccountCode(address, privacyGroupId);

    final String resultCode;
    if (byteCode == BytesValue.EMPTY) {
      resultCode = null;
    } else {
      resultCode = byteCode.toString();
    }
    return new JsonRpcSuccessResponse(request.getId(), resultCode);
  }
}
