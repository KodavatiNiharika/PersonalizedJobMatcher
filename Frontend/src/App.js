import logo from './logo.svg';
import { BrowserRouter } from 'react-router-dom';
import './App.css';
import Dashboard from './pages/Dashboard/Dashboard';
import Login from './pages/Login/Login';
import Navbar from './components/Navbar/Navbar';
import Signup from './pages/Signup/Signup';
import Profile from './pages/Profile/Profile';
import UploadJob from './pages/UploadJobs/UploadJob';
import { Route, Routes } from 'react-router-dom';

function App() {
  return (
    <div className="App"> 
        <Routes>
          <Route path='/dashboard' element={<Dashboard />
}/>
          <Route path='/login' element={<Login/>}/>
          <Route path='/' element={<Signup/>}/>
          <Route path='/navbar' element={<Navbar/>}/>
          <Route path='/profile' element={<Profile/>} />
          <Route path='/jobupload' element={<UploadJob/>} />
        </Routes> 
    </div>
  );
}

export default App;
