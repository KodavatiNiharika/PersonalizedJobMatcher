import logo from './logo.svg';
import { BrowserRouter } from 'react-router-dom';
import './App.css';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login/Login';
import Signup from './pages/Signup/Signup';
import { Route, Routes } from 'react-router-dom';

function App() {
  return (
    <div className="App"> 
        <Routes>
          <Route path='/dashboard' element={<Dashboard/>}/>
          <Route path='/' element={<Login/>}/>
          <Route path='/signup' element={<Signup/>}/>
        </Routes> 
    </div>
  );
}

export default App;
