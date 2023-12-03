/**
 * Copyright 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

const axios = require('axios');

const functions = require('firebase-functions');
const { smarthome } = require('actions-on-google');
const { google } = require('googleapis');
const util = require('util');
const admin = require('firebase-admin');
// Initialize Firebase
admin.initializeApp();
// const firebaseRef = admin.database().ref('/');
// Initialize Homegraph
const auth = new google.auth.GoogleAuth({
  scopes: ['https://www.googleapis.com/auth/homegraph'],
});
const homegraph = google.homegraph({
  version: 'v1',
  auth: auth,
});
// Hardcoded user ID
const USER_ID = '123';  // passed down to all device cloud requests

exports.login = functions.https.onRequest((request, response) => {
  if (request.method === 'GET') {
    functions.logger.log('Requesting login page');
    response.send(`
     <html>
       <meta name="viewport" content="width=device-width, initial-scale=1">
       <body>
         <form action="/login" method="post">
           <input type="hidden"
             name="responseurl" value="${request.query.responseurl}" />
           <button type="submit" style="font-size:14pt">
             Link this service to Google
           </button>
         </form>
       </body>
     </html>
   `);
  } else if (request.method === 'POST') {
    // Here, you should validate the user account.
    // In this sample, we do not do that.
    const responseurl = decodeURIComponent(request.body.responseurl);
    functions.logger.log(`Redirect to ${responseurl}`);
    return response.redirect(responseurl);
  } else {
    // Unsupported method
    response.send(405, 'Method Not Allowed');
  }
});

exports.fakeauth = functions.https.onRequest((request, response) => {
  const responseurl = util.format('%s?code=%s&state=%s',
    decodeURIComponent(request.query.redirect_uri), 'xxxxxx',
    request.query.state);
  functions.logger.log(`Set redirect as ${responseurl}`);
  return response.redirect(
    `/login?responseurl=${encodeURIComponent(responseurl)}`);
});

exports.faketoken = functions.https.onRequest((request, response) => {
  const grantType = request.query.grant_type ?
    request.query.grant_type : request.body.grant_type;
  const secondsInDay = 86400; // 60 * 60 * 24
  const HTTP_STATUS_OK = 200;
  functions.logger.log(`Grant type ${grantType}`);

  let obj;
  if (grantType === 'authorization_code') {
    obj = {
      token_type: 'bearer',
      access_token: '123access',
      refresh_token: '123refresh',
      expires_in: secondsInDay,
    };
  } else if (grantType === 'refresh_token') {
    obj = {
      token_type: 'bearer',
      access_token: '123access',
      expires_in: secondsInDay,
    };
  }
  response.status(HTTP_STATUS_OK)
    .json(obj);
});

const app = smarthome();

// assistant discovery
app.onSync(async (body) => {
  try {
    const devices = await axios.get(` https://d259-2601-646-a080-7c60-d93c-86fd-9f20-c2f4.ngrok-free.app/discover/googleAssistant`);
    // response.status(200).send(devices.data);
    return {
      requestId: body.requestId,
      payload: {
        agentUserId: USER_ID,
        devices: devices.data
      }
    }
  } catch (err) {
    console.log(err);
    // response.status(500).send(err);
  }
});



// assistant query
app.onQuery(async (body) => {
  const { requestId } = body;
  const payload = {
    devices: {},
  };
  const intent = body.inputs[0];
  for (const device of intent.payload.devices) {
    const deviceId = device.id
    try {
      const status = await axios.get(` https://d259-2601-646-a080-7c60-d93c-86fd-9f20-c2f4.ngrok-free.app/query/googleAssistant?id=${deviceId}`);
      payload.devices[deviceId] = status.data.result
    } catch (err) {
      console.log(err);
      payload.devices[deviceId] = {
        on: false
      }
    }
  }
  return {
    requestId: requestId,
    payload: payload,
  };
});

const updateDevice = async (execution, deviceId) => {
  // TODO: Add commands to change device states
};

// assistant control power
app.onExecute(async (body) => {
  const { requestId } = body;
  // Execution results are grouped by status
  const result = {
    ids: [],
    status: 'SUCCESS',
    states: {
      online: true,
    },
  };

  // var powerStates = '';
  // var boardName = '';

  const intent = body.inputs[0];
  for (const command of intent.payload.commands) {
    for (const device of command.devices) {
      const deviceIdArr = device.id.split(':');

      const boardTopic = deviceIdArr[0];
      const deviceId = parseInt(deviceIdArr[1], 10);
      const state = command.execution[0].params.on ? 1 : 0;

      // payload for device cloud
      const payload = { deviceId, boardTopic, state }

      try {
        const resDeviceCloud = await axios.post(` https://d259-2601-646-a080-7c60-d93c-86fd-9f20-c2f4.ngrok-free.app/toggle/googleAssistant`, payload);
        if (resDeviceCloud?.data?.result?.error) throw new Error('Something failed at the device cloud.');
        result.ids.push(device.id);
      } catch (err) {
        console.log(err);
        result.status = 'FAILURE';
        result.states.online = false;
        break;
      }
    }
  }



  return {
    requestId: requestId,
    payload: {
      commands: [result],
    },
  };
});

app.onDisconnect((body, headers) => {
  functions.logger.log('User account unlinked from Google Assistant');
  // Return empty response
  return {};
});

exports.smarthome = functions.https.onRequest(app);

exports.requestsync = functions.https.onRequest(async (request, response) => {
  response.set('Access-Control-Allow-Origin', '*');
  functions.logger.info(`Request SYNC for user ${USER_ID}`);

  // TODO: Call HomeGraph API for user '123'
  response.status(500).send(`Request SYNC not implemented`);
});

/**
 * Send a REPORT STATE call to the homegraph when data for any device id
 * has been changed.
 */
exports.reportstate = functions.database.ref('{deviceId}').onWrite(
  async (change, context) => {
    functions.logger.info('Firebase write event triggered Report State');

    // TODO: Get latest state and call HomeGraph API
  });
