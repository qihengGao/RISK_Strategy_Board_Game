// import React, {useState} from 'react';
// import Table from 'react-bootstrap/Table'
// import AuthService from "../services/auth.service";
// import {DataGrid, GridColDef, GridValueGetterParams, useGridApiRef} from '@mui/x-data-grid';
// import Button from "@mui/material/Button";
// import axios from "axios";
//
//
// function handleRowEditCommit(id){
//     const apiRef = useGridApiRef();
//     const [tableData, setTableData] = useState([...rows]);
//     const model = apiRef.current.getEditRowsModel(); // This object contains all rows that are being editted
//     const newRow = model[id]; // The data that will be commited
//     const name = newRow.name.value; // The new value entered
//     const age = newRow.age.value;
//
//     // Get the row before commiting
//     const oldRow = apiRef.current.getRow(id);
// }
//
//     const unitPlace = ()=>{
//         return (
//             <div>
//                 <Table responsive>
//                     <thead>
//                     <tr>
//                         <th>Territory Name ID</th>
//                         <th>Unit Amount</th>
//                     </tr>
//                     </thead>
//                     <tbody>
//                     {room.riskMap.continent.filter(territory =>
//                         territory.ownerID === AuthService.getCurrentUser().id).map(
//                         (territory, key) => (
//                             <tr>
//                                 <td>{territory.name}</td>
//                                 <td>0</td>
//
//                             </tr>
//                         ))}
//                     </tbody>
//                 </Table>
//             </div>
//
//
//
//
//
//         )
//     }
//
//
//     const orderPlace=()=>{
//         return (
//             <div>
//                 order palce
//             </div>
//         )
//     }
//
//     const waitForOtherPlayer=()=>{
//         return (
//             <div>
//                 order palce
//             </div>
//         )
//     }
//
//
//     // switch (room.state) {
//     //     case "WaitingState":
//     //         return waitForOtherPlayer();
//     //     case "PlacingState":
//     //         return unitPlace();
//     //
//     //     case "OrderingState":
//     //         return orderPlace();
//     //
//     // }
// }
//
// export default playInput;