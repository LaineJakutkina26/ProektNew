class HttpModule {

    status(response) {
        if (response.status >= 200 && response.status < 300) {
            return Promise.resolve(response)
        } else {
            return Promise.reject(new Error(response.statusText))
        }
    }
    json(response) {
        return response.json()
    }
    http(url, method, data) {
        let options = {};
        if (method === 'POST') {
            options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                credentials: 'include',
                body: JSON.stringify(data)
            }
        } else if (method === 'GET') {
            options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                credentials: 'include'
            }
        }
        return fetch(url, options)
                .then(httpModule.status)
                .then(httpModule.json)
                .catch((ex) => console.log("Fetch Exception", ex));
    }
    ;
}
let httpModule = new HttpModule();

export {httpModule};