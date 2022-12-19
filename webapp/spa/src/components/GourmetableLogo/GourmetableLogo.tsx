import { createStyles, Group } from "@mantine/core";
import { Link } from "react-router-dom";
import { ReactComponent as Logo } from "../../assets/images/logo.svg";

const useStyles = createStyles(() => ({
  wrapper: {
    position: "relative",
    width: 180,
    height: 50,
  },
  logo: {
    position: "relative",
    bottom: 4,
    border: "1p solid gray",
    width: 40,
    padding: 0,
    margin: 0,
    alignSelf: "center",
  },
  title: {
    fontWeight: 700,
    fontSize: "1.25rem",
    padding: 0,
    margin: 0,
    alignSelf: "center",
  },
}));
interface LogoProps {
  variant?: "simple" | "default";
}
export function GourmetableLogo({ variant }: LogoProps) {
  const { classes } = useStyles();
  return (
    <Link to="" style={{ textDecoration: "none" }}>
      <Group
        p={0}
        spacing={3}
        className={classes.wrapper}
        style={{ filter: variant === "simple" ? "grayscale(100%)" : "none" }}
      >
        <Logo className={classes.logo} />
        <div className={classes.title}>
          <span style={{ color: "rgb(153, 40, 59)" }}>Gourme</span>
          <span style={{ color: "orange" }}>table</span>
        </div>
      </Group>
    </Link>
  );
}
