import axios from 'axios';
import authHeader from './auth-header';
class UserService {
    getPublicContent() {
        return axios.get(  '/api/test/all');
    }
}
export default new UserService();