import axios from 'axios'

const SERVER_URL = 'http://localhost:9000';

const instance = axios.create({
    baseURL: SERVER_URL,
    timeout: 1000
});

export default {
    // (C)reate
    createNew: (text, active) => instance.post('users', {name: text, active: active}),
    // (R)ead
    getAll: () => instance.get('users', {
        transformResponse: [function (data) {
            return data? JSON.parse(data)._embedded.users : data;
        }]
    }),
    // (U)pdate
    updateForId: (id, text, active) => instance.put('users/'+id, {name: text, active: active}),
    // (D)elete
    removeForId: (id) => instance.delete('users/'+id)
}